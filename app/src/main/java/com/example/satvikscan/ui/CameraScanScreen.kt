package com.example.satvikscan.ui

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.satvikscan.R
import com.example.satvikscan.analyzer.BarcodeAnalyzer
import com.example.satvikscan.analyzer.OcrAnalyzer
import com.example.satvikscan.viewmodel.SatvikViewModel
import com.example.satvikscan.viewmodel.ScanMode
import java.util.concurrent.Executors
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner


@Composable
fun CameraScanScreen(
    viewModel: SatvikViewModel,
    cameramode: ScanMode,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val checkPermission by viewModel.cameraPermission.collectAsState()

    var permissionRequested by rememberSaveable { mutableStateOf(false) }


    var showPermissionDialog by remember { mutableStateOf(false) }

    var hasOpenedSettings by remember { mutableStateOf(false) }


    val uiState = viewModel.productUiState.collectAsState().value
    val imagecaptureReference =remember { mutableStateOf<ImageCapture?>(null) } // made for bhar aces`s use for btn click used
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val ocrAnalyzer = remember { OcrAnalyzer() } // one time make use multiple time
    val barcodeAnalyzer = remember { BarcodeAnalyzer(viewModel, context) }
    val camera = remember { mutableStateOf<Camera?>(null) }
    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }


    val captureExecutor = remember { Executors.newSingleThreadExecutor() }

// when screen close so executor(thread) close
    DisposableEffect(Unit) {
        onDispose {
            try {
                camera.value?.cameraControl?.enableTorch(false)
            } catch (e: Exception) {
                Log.e("CameraScanScreen", "Error disabling torch", e)
            }
            try {
                cameraProviderState.value?.unbindAll()
            } catch (e: Exception) {
                Log.e("CameraScanScreen", "Error unbinding camera", e)
            }
            captureExecutor.shutdown()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {


        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                viewModel.onPermissionResult(hasPermission)

                if (hasOpenedSettings && !hasPermission) {
                    onBackClick()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    //flashlight state
    var isFlashOn by remember { mutableStateOf(false) }
    // eror msg
    val missMatchbarcodeError by viewModel.barcodeError.collectAsState()

    LaunchedEffect(missMatchbarcodeError) {
        if (missMatchbarcodeError != null) {
            kotlinx.coroutines.delay(1800)
            viewModel.clearBarcodeError() // 2.5s baad clear ho jayega
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                try {
                    capturedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                context.contentResolver,
                                it
                            )
                        ) { decoder, _, _ ->
                            // Force SOFTWARE allocation — HARDWARE bitmaps crash ML Kit silently
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        }
                    } else {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    }
                } catch (e: Exception) {
                    Log.e("GalleryPicker", "Failed to decode image", e)
                    capturedBitmap = null
                }
            }
        }
    )

    val activity = context as Activity

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        if (isGranted) {
            viewModel.onPermissionResult(true)
        } else {
            val permanentlyDenied = permissionRequested && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA
                )

            if (permanentlyDenied) {

                showPermissionDialog = true

            } else {
                onBackClick()
            }

        }
    }

    LaunchedEffect(Unit) {
        permissionRequested = true
        cameraLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(cameramode) {
        if (cameramode == ScanMode.BARCODE) {
            viewModel.startScanning()  // isScanning is true
            if (!viewModel.isNetworkAvailable()) {
                viewModel.setOcrError("NO_INTERNET")
                viewModel.stopScanning()
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        if (checkPermission) {
            if (capturedBitmap != null) {
                Box(Modifier.fillMaxSize()) {

                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "capture_image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Buttons Container (Bottom overlay)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Retry Button -> Reset capturedBitmap value to null
                            OutlinedButton(
                                onClick = { capturedBitmap = null },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.camera_btn_retry),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            // OK Button
                            Button(
                                onClick = {
                                    if (cameramode == ScanMode.OCR) {
                                        capturedBitmap?.let { bmp -> // (bmp = capturedBitmap) when not null
                                            // Loader screen trigger karne ke liye
                                            viewModel.startOcrScanning()

                                            // OCR analyze bitmap call callbacks ke saath
                                            ocrAnalyzer.analyzeBitmap(
                                                bitmap = bmp,
                                                onSuccess = { detectedText ->
                                                    // Success par ingredients check aur display ke liye
                                                    Log.d(
                                                        "OCR_DEBUG",
                                                        "Detected text: $detectedText"
                                                    )
                                                    viewModel.processOcrText(detectedText)
                                                },
                                                onFailure = { error ->
                                                    // Failure par error screen dikhane ke liye
                                                    viewModel.setOcrError(
                                                        error.message ?: context.getString(R.string.camera_error_ocr_failed)
                                                    )
                                                }
                                            )
                                        }
                                    } else {
                                        capturedBitmap?.let { bmp ->
                                            // Show loading like OCR mode
                                            viewModel.startOcrScanning()

                                            barcodeAnalyzer.analyzeBitmap(
                                                bitmap = bmp,
                                                onNoBarcodeFound = {

                                                    viewModel.setOcrError(
                                                        context.getString(R.string.camera_error_no_barcode)
                                                    )

                                                },
                                                onFailure = { error ->

                                                    viewModel.setOcrError(
                                                        error.message ?: context.getString(R.string.camera_error_barcode_failed)
                                                    )

                                                }
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.camera_btn_ok),
                                    color = Color(0xFF1B3A2D),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            } else {
                //key(cameramode)  recreate when mode switches
                key(cameramode) {

                    AndroidView(
                        { context ->

                            val previewView = PreviewView(context)

                            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                            cameraProviderFuture.addListener(
                                // which work i follow after got cameraProviderFuture
                                {

                                    val cameraProvider = cameraProviderFuture.get()

                                    cameraProviderState.value = cameraProvider

                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    // for barcoProcessCameraProvider.getInstance(context)de mode camera as fetch barcode
                                    if (cameramode == ScanMode.BARCODE) {
                                        val imageAnalysis = ImageAnalysis.Builder().build()

                                        imageAnalysis.setAnalyzer(
                                            captureExecutor,
                                            barcodeAnalyzer
                                        )
                                        cameraProvider.unbindAll()

                                        camera.value = cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageAnalysis
                                        )

                                    } else {

                                        val imageCapture = ImageCapture.Builder().build()

                                        imagecaptureReference.value = imageCapture

                                        cameraProvider.unbindAll()
                                        camera.value = cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageCapture
                                        )
                                    }

                                },
                                // which i will have to use thread
                                ContextCompat.getMainExecutor(context)
                            )
                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
// custom barcode screen
                    if (cameramode == ScanMode.BARCODE) {
                        ScannerCustomDesign()
                    }

                    // Top bar

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = if (cameramode == ScanMode.BARCODE) stringResource(R.string.camera_title_barcode) else stringResource(R.string.camera_title_ocr),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        // Right spacer to keep title centered
                        Spacer(modifier = Modifier.size(48.dp))
                    }

                    // Hint Text below scanner box
                    if (cameramode == ScanMode.BARCODE) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 220.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.camera_hint_barcode),
                                color = Color.White,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .background(
                                        Color.Black.copy(alpha = 0.55f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ── GALLERY ──
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.18f),   // translucent grey fill
                                    shape = CircleShape          // squircle, not circle
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.35f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .clickable {
                                    galleryLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.gallery),
                                contentDescription = "Gallery",
//                                        tint = Color.White,
                                modifier = Modifier.size(27.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.camera_btn_gallery),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    if (cameramode == ScanMode.OCR) {
                        Spacer(modifier = Modifier.width(15.dp))
                    }

                    // ── FLASH ──
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.18f),
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.35f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .clickable {
                                    isFlashOn = !isFlashOn
                                    camera.value?.cameraControl?.enableTorch(isFlashOn)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (isFlashOn) R.drawable.flash_on_ else R.drawable.flash_off
                                ),
                                contentDescription = "Flash",
                                tint = Color.White,
                                modifier = Modifier.size(29.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.camera_btn_flash),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }

                // OCR Click Button
                if (cameramode == ScanMode.OCR) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 155.dp, start = 3.dp, end = 3.dp)
                            .size(84.dp)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFF19592E), CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    imagecaptureReference.value?.takePicture(
                                        captureExecutor,
                                        object : ImageCapture.OnImageCapturedCallback() {

                                            override fun onCaptureSuccess(image: ImageProxy) {

                                                val imageInfo = image.imageInfo.rotationDegrees
                                                Log.d(
                                                    "ROTATION_DEBUG",
                                                    "rotation degree is : $imageInfo"
                                                )

                                                var bmp = image.toBitmap()

                                                if (imageInfo != 0) {
                                                    val matrix = Matrix()
                                                    matrix.postRotate(imageInfo.toFloat())
                                                    bmp = Bitmap.createBitmap(
                                                        bmp, 0, 0,
                                                        bmp.width, bmp.height,
                                                        matrix, true
                                                    )
                                                }
                                                image.close()

                                                //  CROP LOGIC TO MATCH SCREEN VIEW ──
                                                // Get screen aspect ratio in pixels
                                                val displayMetrics =
                                                    context.resources.displayMetrics
                                                val screenRatio =
                                                    displayMetrics.heightPixels.toFloat() / displayMetrics.widthPixels.toFloat()

                                                Handler(Looper.getMainLooper()).post {
                                                    capturedBitmap = bmp
                                                }

                                                // Get captured bitmap ratio
                                                val bitmapRatio =
                                                    bmp.height.toFloat() / bmp.width.toFloat()

                                                // Crop the sides if the captured image is wider than the preview screen
                                                if (bitmapRatio < screenRatio) {
                                                    val targetWidth =
                                                        (bmp.height / screenRatio).toInt()
                                                    val xOffset = (bmp.width - targetWidth) / 2
                                                    if (targetWidth > 0 && xOffset >= 0 && (xOffset + targetWidth) <= bmp.width) {
                                                        bmp = Bitmap.createBitmap(
                                                            bmp,
                                                            xOffset,
                                                            0,
                                                            targetWidth,
                                                            bmp.height
                                                        )
                                                    }
                                                }

                                            }

                                            override fun onError(exception: ImageCaptureException) {}
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_camera_alt_24),
                                contentDescription = "Capture",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

            }
        }


        // Custom Floating Alert Banner
        AnimatedVisibility(
            visible = missMatchbarcodeError != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFF8B4B4),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xFFFDE8E8),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Error Icon",
                    tint = Color(0xFFC81E1E),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = missMatchbarcodeError ?: "",
                    color = Color(0xFF9B1C1C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (uiState is SatvikViewModel.ProductUiState.Error && uiState.message == "NO_INTERNET") {
            NoInternetDialog(
                onRetry = {
                    viewModel.retryLastScan(onNoBarcodeAndHasInternet = {})
                },
                onDismiss = {
                    onBackClick()
                }
            )
        }

        if (showPermissionDialog) {
            CameraPermissionDialog(
                onDismiss = {
                    showPermissionDialog = false
                    onBackClick()
                },
                onOpenSettings = {
                    showPermissionDialog = false
                    hasOpenedSettings = true
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${context.packageName}")
//                        Uri.fromParts("package", context.packageName, null)
                    )

                    context.startActivity(intent)
                }
            )
        }
    }

}

@Composable
fun CameraPermissionDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .width(350.dp)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.permison),
                    contentDescription = "Camera Permission",
                    modifier = Modifier
                        .size(150.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(R.string.camera_permission_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.camera_permission_desc),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF08733A)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.camera_permission_cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00C853),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.camera_permission_settings),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// custome barcode screen

@Composable
fun ScannerCustomDesign(modifier: Modifier = Modifier) {
    // 1. Transition and Animation Fraction (runs from 0f to 1f)
    val infiniteTransition = rememberInfiniteTransition()
    val animatedFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // box width & hight according entire screen
        val boxWidth = width * 0.75f // take 75%
        val boxHeight = boxWidth * 0.75f // for rectangle

        // Center position
        val left = (width - boxWidth) / 2   // remaining space of one side
        val top = (height - boxHeight) / 2.3f  // not a center because 2.3 greater of half

        val right = left + boxWidth
        val bottom = top + boxHeight

        val overlayPath = Path().apply {
//
            addRect(Rect(0f, 0f, width, height)) // puri screen black
            addRoundRect(
                RoundRect(
                    rect = Rect(left, top, right, bottom),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()) // 24.dp rounded corners
                )
            )
            fillType = PathFillType.EvenOdd
        }

        drawPath(path = overlayPath, color = Color.Black.copy(alpha = 0.55f))

        val radius = 24.dp.toPx()
        val cornerLen = 43.dp.toPx()
        val stroke = 6.dp.toPx()
        val bracketColor = Color(0xFF08733A)

        // Top-left
        val topLeft = Path().apply {
            moveTo(left, top + cornerLen)
            lineTo(left, top + radius)
            arcTo(Rect(left, top, left + 2 * radius, top + 2 * radius), 180f, 90f, false)
            lineTo(left + cornerLen, top)
        }
        drawPath(topLeft, bracketColor, style = Stroke(stroke, cap = StrokeCap.Round))

        // Top-right
        val topRight = Path().apply {
            moveTo(right - cornerLen, top)
            lineTo(right - radius, top)
            arcTo(Rect(right - 2 * radius, top, right, top + 2 * radius), 270f, 90f, false)
            lineTo(right, top + cornerLen)
        }
        drawPath(topRight, bracketColor, style = Stroke(stroke, cap = StrokeCap.Round))

        // Bottom-left
        val bottomLeft = Path().apply {
            moveTo(left, bottom - cornerLen)
            lineTo(left, bottom - radius)
            arcTo(Rect(left, bottom - 2 * radius, left + 2 * radius, bottom), 180f, -90f, false)
            lineTo(left + cornerLen, bottom)
        }
        drawPath(bottomLeft, bracketColor, style = Stroke(stroke, cap = StrokeCap.Round))

        // Bottom-right
        val bottomRight = Path().apply {
            moveTo(right - cornerLen, bottom)
            lineTo(right - radius, bottom)
            arcTo(Rect(right - 2 * radius, bottom - 2 * radius, right, bottom), 90f, -90f, false)
            lineTo(right, bottom - cornerLen)
        }
        drawPath(bottomRight, bracketColor, style = Stroke(stroke, cap = StrokeCap.Round))

        // thoda space because dont lien touch to scanner border
        val verticalPadding = 20.dp.toPx()

        // . Calculate lineY from the animatedFraction
        val lineY =
            (top + verticalPadding) +
                    ((bottom - verticalPadding) - (top + verticalPadding)) * animatedFraction

        // 4. Draw horizontal scanning line
        drawLine(
            color = bracketColor.copy(alpha = 0.8f),
            start = Offset(left + 8.dp.toPx(), lineY),
            end = Offset(right - 8.dp.toPx(), lineY),
            strokeWidth = 4.dp.toPx()
        )

    }
}
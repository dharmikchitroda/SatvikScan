package com.example.satvikscan.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.satvikscan.R
import com.example.satvikscan.analyzer.BarcodeAnalyzer
import com.example.satvikscan.analyzer.OcrAnalyzer
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import com.example.satvikscan.ui.viewmodel.ScanMode
import java.util.concurrent.Executors


@Composable
fun CameraScanScreen(
    viewModel: Satvik_ViewModel,
    cameramode: ScanMode
) {
    val context = LocalContext.current
    val checkPermission by viewModel.CameraPermission.collectAsState()
    val imagecaptureReference =
        remember { mutableStateOf<ImageCapture?>(null) } // made for bhar aces`s use for btn click used
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val ocrAnalyzer = remember { OcrAnalyzer() } // one time make use multiple time
    val barcodeAnalyzer = remember { BarcodeAnalyzer(viewModel, context) }
    val camera = remember {
        mutableStateOf<Camera?>(null)
    }
    //flashlight state
    var isFlashOn by remember {
        mutableStateOf(false)
    }
    // eror msg
    val MissMatchbarcodeError by viewModel.barcodeError.collectAsState()

    LaunchedEffect(MissMatchbarcodeError) {
    if (MissMatchbarcodeError != null) {
        kotlinx.coroutines.delay(1800)
        viewModel.clearBarcodeError() // 2.5s baad clear ho jayega
    }
}
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                //   above android 8
                capturedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            context.contentResolver,
                            it
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                }
            }
        }
    )


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.PermissionResult(isGranted)
        } else {
            // future plan when user permission is deny while
            // go to-> direct setting and show popup
        }
    }

    LaunchedEffect(Unit) {
        cameraLauncher.launch(Manifest.permission.CAMERA)

    }

    LaunchedEffect(cameramode) {
        if (cameramode == ScanMode.BARCODE) {
            viewModel.startScanning()  // isScanning is true
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
                                    "↺  Retry",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            // OK Button
                            Button(
                                onClick = {
                                    if (cameramode == ScanMode.OCR) {
                                        capturedBitmap?.let { bmp -> // (bmp = capturedBitmap) when not nulll
                                            // 1. Loader screen trigger karne ke liye
                                            viewModel.startOcrScanning()

                                            // 2. OCR analyze bitmap call callbacks ke saath
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
                                                        error.message ?: "OCR recognition failed"
                                                    )
                                                }
                                            )
                                        }
                                    } else {
                                        capturedBitmap?.let { bmp ->
                                            barcodeAnalyzer.analyzeBitmap(bmp)

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
                                    "✓  OK",
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
                                {
                                    val cameraProvider = cameraProviderFuture.get()

                                    val preview = Preview.Builder().build()
                                    preview.setSurfaceProvider(previewView.surfaceProvider)

                                    if (cameramode == ScanMode.BARCODE) {
                                        val imageAnalysis = ImageAnalysis.Builder().build()

                                        imageAnalysis.setAnalyzer(
                                            Executors.newSingleThreadExecutor(),
                                            BarcodeAnalyzer(viewModel, context)
                                        )
                                        cameraProvider.unbindAll()

                                        camera.value = cameraProvider.bindToLifecycle(
                                            context as androidx.lifecycle.LifecycleOwner,
                                            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageAnalysis
                                        )

                                    } else {
                                        val imageCapture = ImageCapture.Builder().build()
                                        imagecaptureReference.value = imageCapture
                                        cameraProvider.unbindAll()
                                        camera.value = cameraProvider.bindToLifecycle(
                                            context as androidx.lifecycle.LifecycleOwner,
                                            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageCapture
                                        )
                                    }

                                },
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
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = if (cameramode == ScanMode.BARCODE) "Scan QR" else "Capture Ingredients",
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
                                text = "Align label inside box",
                                color = Color.White,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .background(
                                        Color.Black.copy(alpha = 0.55f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }}


                        // Bottom bar
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(top = 20.dp, bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Gallery + Flash Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 60.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // ── GALLERY ──
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,

                                    ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(
                                                color = Color.White.copy(alpha = 0.18f),   // translucent grey fill
                                                shape = RoundedCornerShape(18.dp)           // squircle, not circle
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = Color.White.copy(alpha = 0.35f),
                                                shape = RoundedCornerShape(18.dp)
                                            )
                                            .clickable {
                                                galleryLauncher.launch("image/*")
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.gallery),
                                            contentDescription = "Gallery",
//                                        tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Text(
                                        text = "GALLERY",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }

                                // ── FLASH ──
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(
                                                color = Color.White.copy(alpha = 0.18f),
                                                shape = RoundedCornerShape(18.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = Color.White.copy(alpha = 0.35f),
                                                shape = RoundedCornerShape(18.dp)
                                            )
                                            .clickable {
                                                isFlashOn = !isFlashOn
                                                camera.value?.cameraControl?.enableTorch(isFlashOn)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.flash_on),
                                            contentDescription = "Flash",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Text(
                                        text = "FLASH",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }


                }
                if (cameramode == ScanMode.OCR) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 150.dp)
                            .size(72.dp)
                            .background(Color(0xFF08733A), CircleShape)
                            .border(3.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            .clickable {
                                imagecaptureReference.value?.takePicture(
                                    Executors.newSingleThreadExecutor(),
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

                                            Handler(Looper.getMainLooper()).post {
                                                capturedBitmap = bmp
                                            }
                                        }

                                        override fun onError(exception: ImageCaptureException) {}
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                            contentDescription = "Capture",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

            }
        }


    // Custom Floating Alert Banner
    AnimatedVisibility(
        visible = MissMatchbarcodeError != null,
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
                text = MissMatchbarcodeError ?: "",
                color = Color(0xFF9B1C1C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
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
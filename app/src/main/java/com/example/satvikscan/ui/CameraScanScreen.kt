package com.example.satvikscan.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

    val checkPermission by viewModel.CameraPermission.collectAsState()
    val imagecaptureReference =
        remember { mutableStateOf<ImageCapture?>(null) } // made for bhar acess use for btn click used
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val ocrAnalyzer = remember { OcrAnalyzer() } // one time make use multiple time


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
            viewModel.startScanning()  // isScanning is tru '/8
            // e
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
                                    capturedBitmap?.let { bmp -> // (bmp = capturedBitmap) when not nulll
                                        // 1. Loader screen trigger karne ke liye
                                        viewModel.startOcrScanning()

                                        // 2. OCR analyze bitmap call callbacks ke saath
                                        ocrAnalyzer.analyzeBitmap(
                                            bitmap = bmp,
                                            onSuccess = { detectedText ->
                                                // Success par ingredients check aur display ke liye
                                                Log.d("OCR_DEBUG", "Detected text: $detectedText")
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
                                        cameraProvider.bindToLifecycle(
                                            context as androidx.lifecycle.LifecycleOwner,
                                            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageAnalysis
                                        )
                                    } else {
                                        val imageCapture = ImageCapture.Builder().build()
                                        imagecaptureReference.value = imageCapture
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
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
                    if (cameramode == ScanMode.BARCODE) {
                        ScannerCustomDesign()
                    }
                }
                if (cameramode == ScanMode.OCR) {
                    Button(
                        onClick = {
                            imagecaptureReference.value?.takePicture(
                                Executors.newSingleThreadExecutor(), // background thread
                                object : ImageCapture.OnImageCapturedCallback() {

                                    override fun onCaptureSuccess(image: ImageProxy) {

                                        val imageInfo = image.imageInfo.rotationDegrees
                                        Log.d("ROTATION_DEBUG", "rotation degree is : $imageInfo")

                                        var bmp = image.toBitmap()  // ImageProxy → Bitmap
                                        if (imageInfo != 0) {
                                            val matrix = Matrix()
                                            matrix.postRotate(imageInfo.toFloat())
                                            bmp = Bitmap.createBitmap(
                                                bmp,
                                                0,
                                                0,
                                                bmp.width,
                                                bmp.height,
                                                matrix,
                                                true
                                            )
                                        }
                                        image.close() // image band karo

                                        Handler(Looper.getMainLooper()).post {
                                            capturedBitmap = bmp   // main thread pe state update
                                        }

                                    }

                                    override fun onError(exception: ImageCaptureException) {

                                    }
                                }

                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 40.dp)
                            .size(72.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = "Capture",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

            }
        }
    }


}


@androidx.compose.ui.tooling.preview.Preview(showBackground = true,showSystemUi = true)
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
            addRect(Rect(0f, 0f, width, height))
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
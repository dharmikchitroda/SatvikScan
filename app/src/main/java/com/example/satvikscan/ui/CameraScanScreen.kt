package com.example.satvikscan.ui

import android.Manifest
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.satvikscan.R
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    val imagecaptureReference = remember { mutableStateOf<ImageCapture?>(null) } // made for bhar acess use for btn click used
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val ocrAnalyzer = remember { OcrAnalyzer() } // obne time make use multiple time


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
                              capturedBitmap?.let { bmp -> // bmp = capturedBitmap when not nulll
                                  ocrAnalyzer.analyzeBitmap(bmp)  //  (bmp) value give it to function
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
      }
      else{
          //key(cameramode)  recreate when mode switches
              key(cameramode) {
                  AndroidView(
                      //factory run once time, first composition, one-time initialization

                      factory = { context ->

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
              }
              if (cameramode == ScanMode.OCR) {
                  Button(
                      onClick = {

                          imagecaptureReference.value?.takePicture(
                              Executors.newSingleThreadExecutor(), // background thread
                              object : ImageCapture.OnImageCapturedCallback() {

                                  override fun onCaptureSuccess(image: ImageProxy) {
                                      val bmp = image.toBitmap()  // ImageProxy → Bitmap
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
                          painter = painterResource(id=R.drawable.baseline_camera_alt_24),
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

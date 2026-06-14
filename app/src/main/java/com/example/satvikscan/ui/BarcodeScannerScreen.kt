package com.example.satvikscan.ui

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.satvikscan.analyzer.BarcodeAnalyzer
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import java.util.concurrent.Executors

@Composable
fun BarcodeScannercameraScreen(
    viewModel: Satvik_ViewModel,
) {

    val CheckPermisson by viewModel.CameraPermission.collectAsState()


    val cameraluncher = rememberLauncherForActivityResult(
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
        cameraluncher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize()) {
        if (CheckPermisson) {
            AndroidView(
                factory = { context -> // this context would use many time below code
                    // context will provided by system

                    val previewView = PreviewView(context)

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                    cameraProviderFuture.addListener(
                        {

                            // don't use listener ke bahar because sometime camera not be ready and i give a object so app happen to the crash
                            val cameraProvider = cameraProviderFuture.get()

                            // get every frame from the camera
                            val preview = Preview.Builder().build()

                            // provide a surface to the preview
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            //for join analyser
                            val imageAnalysis = ImageAnalysis.Builder().build()

                            imageAnalysis.setAnalyzer(
                                Executors.newSingleThreadExecutor(), //background thred use
                                BarcodeAnalyzer(viewModel, context)
                            )

                            cameraProvider.unbindAll()

                            cameraProvider.bindToLifecycle(
                                context as androidx.lifecycle.LifecycleOwner,
                                androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )

                        },
                        ContextCompat.getMainExecutor(context) // main thread
                    )
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

    }

}

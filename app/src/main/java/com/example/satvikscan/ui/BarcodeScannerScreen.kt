package com.example.satvikscan.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.satvikscan.ui.theme.Satvik_ViewModel

@Composable
fun BarcodeScannercameraScreen(viewModel: Satvik_ViewModel ) {

    val CheckPermisson by viewModel.CameraPermission.collectAsState()


    val cameraluncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.PermissionResult(isGranted)
        } else {

        }
    }

    LaunchedEffect(Unit) {
        cameraluncher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize()) {
      if(CheckPermisson)  {
            AndroidView(
                factory = { context -> // this context would use manytime below code
                    // context will provided by system

                    val previewView = PreviewView(context)

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                    cameraProviderFuture.addListener({

                        // don't use listener ke BAHAR beciuse sometime camera not be ready and i give a object so app happen to the crash
                        val cameraProvider = cameraProviderFuture.get()

                        // get every frame froma the camera
                        val preview = Preview.Builder().build()

                        // provide a surface to the preview
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            context as androidx.lifecycle.LifecycleOwner,
                            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                            preview
                        )

                    }, ContextCompat.getMainExecutor(context))

                    previewView
                }
            )
        }

    }

}


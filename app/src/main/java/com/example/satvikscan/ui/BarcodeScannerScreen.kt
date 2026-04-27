package com.example.satvikscan.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BarcodeScannerScreen() {

    val cameraluncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

        } else {

        }
    }

    LaunchedEffect(Unit) {
        cameraluncher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val previewView = androidx.camera.view.PreviewView(context)
                val cameraProviderFuture = androidx.camera.lifecycle.ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({

                    val cameraProvider = cameraProviderFuture.get()

                    val preview = androidx.camera.core.Preview.Builder().build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)


                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        context as androidx.lifecycle.LifecycleOwner,
                        androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
                    )

                },androidx.core.content.ContextCompat.getMainExecutor(context))

                previewView
            }
        )
    }

}


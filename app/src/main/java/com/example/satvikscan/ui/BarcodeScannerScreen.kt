package com.example.satvikscan.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import android.Manifest

@Composable
fun BarcodeScannerScreen() {

    val cameraluncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted  ->
        if (isGranted){

        }
        else {

        }
    }

    LaunchedEffect(Unit) {
        cameraluncher.launch( Manifest.permission.CAMERA)
   }
}


package com.example.satvikscan.analyzer

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer : ImageAnalysis.Analyzer {


    private val option = BarcodeScannerOptions.Builder().build() // configuration/settings of barcode scanner used for only custumize aur add somthing advance



    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image : ImageProxy) {

        val mediaImage = image.image

        if (mediaImage != null) {

            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )

            val scanner = BarcodeScanning.getClient(option)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val value = barcode.rawValue ?: continue // if value does not exist so move next(continue)
                        Log.d("BARCODE", value)
                    }
                }
                .addOnFailureListener {
                    //scan fail
                }
                .addOnCompleteListener {
                    //kaam khatam
                    image.close()
                }
        }
    }

}
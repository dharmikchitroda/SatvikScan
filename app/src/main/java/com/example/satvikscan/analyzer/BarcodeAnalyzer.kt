package com.example.satvikscan.analyzer

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val viewModel: Satvik_ViewModel) : ImageAnalysis.Analyzer {


    private val option = BarcodeScannerOptions.Builder()
        .build() // configuration/settings of barcode scanner used for only custumize aur add somthing advance


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        // check resut and close the camera till user not open rescan
        if (!viewModel.isScanning.value) {
            image.close()
            return
        }

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

                        // stop scaning when result number
                        viewModel.stopscanning()

                    }
                }
                .addOnFailureListener {
                    //scan fail
                }
                .addOnCompleteListener {
                    image.close()
                }
        }
    }
}
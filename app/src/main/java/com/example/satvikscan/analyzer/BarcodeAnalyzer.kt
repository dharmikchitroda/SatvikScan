package com.example.satvikscan.analyzer

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val viewModel: Satvik_ViewModel, private val context: Context) :
    ImageAnalysis.Analyzer {


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

                        // small vibrate
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                        //  this work only Android 26+ so check first
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    150,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(150)
                        }
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
package com.example.satvikscan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.os.Vibrator
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.satvikscan.R
import com.example.satvikscan.viewmodel.SatvikViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val viewModel: SatvikViewModel, private val context: Context) :
    ImageAnalysis.Analyzer {


    // configuration/settings of barcode scanner with supported formats to improve performance
    private val option = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_ITF,
            Barcode.FORMAT_QR_CODE
        )
        .build()
    private val scanner = BarcodeScanning.getClient(option)

    private var lastErrorTimestamp = 0L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        // check result and close the camera till user not open rescan
        if (!viewModel.isScanning.value) {
            image.close()
            return
        }

        val mediaImage = image.image

        if (mediaImage == null) {
            image.close()
            return
        }

        try {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )

            processImage(inputImage) {
                image.close()
            }
        } catch (e: Exception) {
            Log.e("BarcodeAnalyzer", "Failed to process image frame", e)
            image.close()
        }
    }

    fun analyzeBitmap(
        bitmap: Bitmap,
        onNoBarcodeFound: (() -> Unit)? = null,
        onFailure: ((Exception) -> Unit)? = null
    ) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        processImage(inputImage, onNoBarcodeFound = onNoBarcodeFound, onScanFailure = onFailure)
    }

    private fun processImage(
        inputImage: InputImage,
        onComplete: (() -> Unit)? = null,
        onNoBarcodeFound: (() -> Unit)? = null,
        onScanFailure: ((Exception) -> Unit)? = null
    ) {

        // small vibrate
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                var foundValidBarcode = false

                for (barcode in barcodes) {

                    val barcodeFormat = barcode.format

                    when(barcodeFormat){
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_UPC_E,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_CODE_39,
                        Barcode.FORMAT_CODE_93,
                        Barcode.FORMAT_ITF-> {
                            // if value does not exist so move next(continue)
                            val value = barcode.rawValue?: continue

                            try {
                                vibrator?.vibrate(150)
                            } catch (e: Exception) {
                                Log.e("BarcodeAnalyzer", "Vibration failed", e)
                            }

                            // send to code viewmodel and would call api
                            viewModel.changeProductUiState(value)

                            // stop scaning when result number
                            viewModel.stopScanning()

                            foundValidBarcode = true
                            break
                        }
                        Barcode.FORMAT_QR_CODE -> {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastErrorTimestamp > 2500) {
                                lastErrorTimestamp = currentTime
                                Log.d("Barcode", "no THIS IS PAYMENT QR")
                                viewModel.setBarcodeError(context.getString(R.string.camera_error_qr_code))
                            }
                        }
                        else -> {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastErrorTimestamp > 2500) {
                                lastErrorTimestamp = currentTime
                                viewModel.setBarcodeError(context.getString(R.string.camera_error_unsupported_format))
                            }
                        }
                    }


                }

                // No valid barcode found in the image
                if (!foundValidBarcode && onNoBarcodeFound != null) {
                    onNoBarcodeFound()
                }
            }
            .addOnFailureListener {
                //scan fail
                onScanFailure?.invoke(it)
            }
            .addOnCompleteListener {
                onComplete?.invoke()
            }
    }
}
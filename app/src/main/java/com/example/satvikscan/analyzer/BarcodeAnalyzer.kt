package com.example.satvikscan.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer : ImageAnalysis.Analyzer {


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        val mediaImage = image.image

        if (mediaImage != null) {

            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )
        }
    }
}
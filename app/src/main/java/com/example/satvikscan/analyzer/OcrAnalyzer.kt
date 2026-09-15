package com.example.satvikscan.analyzer

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.example.satvikscan.viewmodel.OcrTextValidator
import com.example.satvikscan.viewmodel.ValidationResult

class OcrAnalyzer {

    // ML Kit text recognizer object
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun analyzeBitmap(
        bitmap: Bitmap,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(inputImage)

            .addOnSuccessListener { visionText ->

                val extractedText = visionText.text

                val result = OcrTextValidator.validate(extractedText)
                when (result) {

                    is ValidationResult.Valid -> {
                        onSuccess(extractedText)
                    }

                    is ValidationResult.Invalid -> {
                        onFailure(
                            Exception(result.message)
                        )
                    }
                }

            }.addOnFailureListener { e ->
                // error aane par callback call hoga
                onFailure(e)

            }.addOnCompleteListener {
                // ZARURI hai, warna camera hang ho jaata
            }
    }
}

package com.example.satvikscan.analyzer

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrAnalyzer {

    // ML Kit text recognizer object
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun analyzeBitmap(bitmap: Bitmap) {

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(inputImage).addOnSuccessListener { visionText ->
            if (visionText.text.isBlank()) {
                Log.d("OCR_RESULT", "No text found in image.")
            } else {
                Log.d("OCR_RESULT", "=== Extracted Text ===")
                Log.d("OCR_RESULT", visionText.text)
                Log.d("OCR_RESULT", "======================")
            }
        }.addOnFailureListener { e ->
            Log.e("OCR_RESULT", "Failed: ${e.message}")
        }.addOnCompleteListener {
            // ZARURI hai, warna camera hang ho jaata
        }
    }
}

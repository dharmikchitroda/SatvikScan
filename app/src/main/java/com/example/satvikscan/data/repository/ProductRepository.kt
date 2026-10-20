package com.example.satvikscan.data.repository

import android.util.Log
import com.example.satvikscan.Api_Calling.ApiService
import com.example.satvikscan.data.model.Product
import com.example.satvikscan.data.model.ProductResponse
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ProductRepository(private val apiService: ApiService) {

    private val firestore = FirebaseFirestore.getInstance()

    // FUTURE TOGGLE:
    // just false conection break api calling to oepn foodfacr
    private val ENABLE_OPENFOODFACTS_FALLBACK = true

    suspend fun getProduct(barcode: String): ProductResponse {
        try {
            // check documnent Firebase Firestore
            val doc = fetchFirestoreProduct(barcode)

            if (doc != null && doc.exists()) {
                val status = doc.getString("verificationStatus")
                Log.d("ProductRepo", "Firestore barcode $barcode status: $status")

                //  take only verified
                if (status == "VERIFIED") {
                    val name = doc.getString("productName") ?: "Unknown Product"
                    val ingredients = doc.getString("ingredientsText") ?: ""

                    // Images: frontImageUrl, imageUrl or nested images.front
                    val frontImg = doc.getString("frontImageUrl")
                        ?: doc.getString("imageUrl")
                        ?: (doc.get("images") as? Map<*, *>)?.get("front") as? String

                    Log.d("ProductRepo", "Serving 100% VERIFIED product from Firebase: $name")

                    return ProductResponse(
                        code = barcode,
                        status = 1,
                        product = Product(
                            productName = name,
                            ingredientsText = ingredients,
                            imageUrl = frontImg,
                            imageThumbUrl = frontImg,
                            imageSmallUrl = frontImg
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepo", "Firebase lookup error for $barcode: ${e.message}")
        }

        // if didnt fetch verified for perticular barcode so   fallback to OpenFoodFacts
        return if (ENABLE_OPENFOODFACTS_FALLBACK) {
            Log.d("ProductRepo", "Falling back to OpenFoodFacts API for barcode: $barcode")
            apiService.getProductDetails(barcode)
        } else {
            ProductResponse(code = barcode, status = 0, product = null)
        }
    }

    private suspend fun fetchFirestoreProduct(barcode: String): DocumentSnapshot? =
        suspendCancellableCoroutine { continuation ->
            firestore.collection("products").document(barcode).get()
                .addOnSuccessListener { doc ->
                    if (continuation.isActive) continuation.resume(doc)
                }
                .addOnFailureListener { e ->
                    Log.w("ProductRepo", "Firestore error: ${e.message}")
                    if (continuation.isActive) continuation.resume(null)
                }
        }
}
package com.example.satvikscan.data.repository

import android.util.Log
import com.example.satvikscan.Api_Calling.ApiService
import com.example.satvikscan.data.model.Product
import com.example.satvikscan.data.model.ProductResponse
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// with use dependecy injection (DI) , call time inject object , will should use  hilt , take paramter obeject of reposetry

class ProductRepository(private val apiService: ApiService) {

    private val firestore = FirebaseFirestore.getInstance()

    // FUTURE TOGGLE:
    // just false conection break api calling to oepn foodfacr
    // Cloud switch: settings/app_config se bina app update ke control

    suspend fun getProduct(barcode: String): ProductResponse {
        Log.d("Barcode", " Barcode Scanned / Fetching: $barcode")
        try {
            // check documnent Firebase Firestore
            val doc = fetchFirestoreDoc("products", barcode)

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

                    // Back Image: backImageUrl or nested images.ingredients
                    val backImg = doc.getString("backImageUrl")
                        ?: (doc.get("images") as? Map<*, *>)?.get("ingredients") as? String

                    Log.d("ProductRepo", "Serving 100% VERIFIED product from Firebase: $name")

                    return ProductResponse(
                        code = barcode,
                        status = 1,
                        product = Product(
                            productName = name,
                            ingredientsText = ingredients,
                            imageUrl = frontImg,
                            imageThumbUrl = frontImg,
                            imageSmallUrl = frontImg,
                            backImageUrl = backImg
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepo", "Firebase lookup error for $barcode: ${e.message}")
        }

        // if didnt fetch verified for perticular barcode so   fallback to OpenFoodFacts
        // Cloud Switch: settings/app_config se check karo (Default = true)
        val isFallbackEnabled = fetchFirestoreDoc("settings", "app_config")?.getBoolean("enableOpenFoodFacts") ?: true

        return if (isFallbackEnabled) {
            Log.d("ProductRepo", "Falling back to OpenFoodFacts API for barcode: $barcode")
            apiService.getProductDetails(barcode)
        } else {
            ProductResponse(code = barcode, status = 0, product = null)
        }
    }

    private suspend fun fetchFirestoreDoc(collection: String, documentId: String): DocumentSnapshot? =
        suspendCancellableCoroutine { continuation ->
            firestore.collection(collection).document(documentId).get()
                .addOnSuccessListener { doc ->
                    if (continuation.isActive) continuation.resume(doc)
                }
                .addOnFailureListener { e ->
                    Log.w("ProductRepo", "Firestore error on $collection/$documentId: ${e.message}")
                    if (continuation.isActive) continuation.resume(null)
                }
        }
}
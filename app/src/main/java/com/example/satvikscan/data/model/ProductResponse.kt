package com.example.satvikscan.data.model

import com.google.gson.annotations.SerializedName

// dataclass accroding our need fetch from api data
data class ProductResponse(
    @SerializedName("code")
    val code: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("product")
    val product: Product? // data type Product class - this object inside object
)

data class Product(
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("ingredients_text")
    val ingredientsText: String?,
    @SerializedName("image_url")
    val imageUrl: String?,              // main display photo
    @SerializedName("image_thumb_url")
    val imageThumbUrl: String?,         // for history rows
    @SerializedName("image_small_url")
    val imageSmallUrl: String?          // small image
)

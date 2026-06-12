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
    val ingredientsText: String?
)

class Hello {
    fun area() = println("den")
}
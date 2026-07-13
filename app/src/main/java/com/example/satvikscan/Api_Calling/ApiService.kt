package com.example.satvikscan.Api_Calling

import com.example.satvikscan.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductDetails(@Path("barcode") barcode: String): ProductResponse

}
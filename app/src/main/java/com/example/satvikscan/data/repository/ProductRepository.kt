package com.example.satvikscan.data.repository

import com.example.satvikscan.Api_Calling.ApiService
import com.example.satvikscan.data.model.ProductResponse

// with use dependecy injection (DI) , call time inject object , will should use  hilt , take paramter obeject of reposetry

class ProductRepository(private val apiService: ApiService) {

    suspend fun getProduct(barcode: String): ProductResponse {
        return apiService.getProductDetails(barcode)
    }
}
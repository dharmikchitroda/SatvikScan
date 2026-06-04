package com.example.satvikscan.Api_Calling

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// made a singletone obj
object RetrofitInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java) // internally generated class object
    }

}


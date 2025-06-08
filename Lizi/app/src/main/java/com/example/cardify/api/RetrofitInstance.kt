// com.example.cardify.api.RetrofitInstance.kt

package com.example.cardify.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: CardifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://3.219.85.146:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardifyApi::class.java)
    }
}

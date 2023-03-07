package com.guerin.velovify

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api : VelovApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jcdecaux.com/vls/v3/stations/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VelovApi::class.java)
    }
}
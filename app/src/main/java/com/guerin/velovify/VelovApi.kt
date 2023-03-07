package com.guerin.velovify

import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Call
import retrofit2.http.GET
import com.guerin.velovify.objects.StationsItem
import retrofit2.http.Query

interface VelovApi {
    @GET("?contract=lyon&")
    fun getStations(@Query("apiKey") apiKey: String): Call<List<StationsItem>>
}
package com.abdulaziz.provalyutakurslari.retrofit

import com.abdulaziz.provalyutakurslari.models.Currency
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("json")
    fun getCurrency(): Call<List<Currency>>

}
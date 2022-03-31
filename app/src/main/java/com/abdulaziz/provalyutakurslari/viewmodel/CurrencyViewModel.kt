package com.abdulaziz.provalyutakurslari.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrencyViewModel : ViewModel() {

    private var liveData = MutableLiveData<List<Currency>>()

    fun getCurrency(): LiveData<List<Currency>> {
        ApiClient.apiService.getCurrency().enqueue(object : Callback<List<Currency>>{

            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>,
            ) {
                if (response.isSuccessful){
                    liveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {

            }

        })
        return liveData
    }

}
package com.abdulaziz.provalyutakurslari.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abdulaziz.provalyutakurslari.db.CurrencyDatabase
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.models.Information
import com.abdulaziz.provalyutakurslari.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyWork(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private lateinit var appDatabase: CurrencyDatabase

    override fun doWork(): Result {
        appDatabase = CurrencyDatabase.getInstance(context)

        ApiClient.apiService.getCurrency().enqueue(object : Callback<List<Currency>> {
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>,
            ) {
                if (response.isSuccessful) {
                    saveData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {

            }

        })

        return Result.success()
    }

    private fun saveData(currencylist: List<Currency>) {
        var informationId = 1
        if (appDatabase.currencyDao().getLastInformation() != null) {
            informationId = appDatabase.currencyDao().getLastInformation()!!.informationId + 1
        }
        appDatabase.currencyDao()
            .insertInformationForCurrency(Information(informationId), currencylist)
    }


    override fun onStopped() {
        super.onStopped()
        appDatabase.close()
    }
}
package com.abdulaziz.provalyutakurslari.db.dao

import androidx.room.*
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.models.Information
import com.abdulaziz.provalyutakurslari.models.InformationWithCurrency

@Dao
interface CurrencyDao {

    fun insertInformationForCurrency(information: Information, currency: List<Currency>){
        for (cur in currency){
            cur.informationIdForCurrency = information.informationId
        }

        insertAll(currency)
        insertInfo(information)
    }

    @Query("select * from information order by informationId desc limit 1")
    fun getLastInformation() : Information?

    @Insert
    fun insertInfo(information: Information)

    @Insert
    fun insertAll(currency: List<Currency>)

    @Transaction
    @Query("select * from information where informationId in (select max(informationId) from information)")
    fun getInformationWithCurrencyLast() : InformationWithCurrency?

    @Transaction
    @Query("select * from information")
    fun getInformationWithCurrency() : List<InformationWithCurrency>?

}
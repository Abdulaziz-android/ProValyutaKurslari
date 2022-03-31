package com.abdulaziz.provalyutakurslari.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val cb_price: String,
    val code: String,
    val date: String,
    val nbu_buy_price: String,
    val nbu_cell_price: String,
    val title: String,
    var informationIdForCurrency: Int
):Serializable
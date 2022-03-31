package com.abdulaziz.provalyutakurslari.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Information (
    @PrimaryKey(autoGenerate = true)
    var informationId:Int = 1
)
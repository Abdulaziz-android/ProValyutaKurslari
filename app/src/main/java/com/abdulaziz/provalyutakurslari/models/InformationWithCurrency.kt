package com.abdulaziz.provalyutakurslari.models

import androidx.room.Embedded
import androidx.room.Relation

data class InformationWithCurrency(
    @Embedded val information: Information,
    @Relation(
        parentColumn = "informationId",
        entityColumn = "informationIdForCurrency"
    )
    val list: List<Currency>
)

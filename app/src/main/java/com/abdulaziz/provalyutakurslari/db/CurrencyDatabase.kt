package com.abdulaziz.provalyutakurslari.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdulaziz.provalyutakurslari.db.dao.CurrencyDao
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.models.Information

@Database(entities = [Currency::class, Information::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        private var db: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context.applicationContext, CurrencyDatabase::class.java,
                    "my_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return db!!
        }
    }
}
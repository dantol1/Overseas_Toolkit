package com.dantol.overseastoolkit

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dantol.overseastoolkit.main.currency.database.CurrencyDBConverter
import com.dantol.overseastoolkit.main.currency.database.ExchangeRateDao
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate

@Database(
	entities = [
		ExchangeRate::class
	],
	version = 1,
	exportSchema = false
)
@TypeConverters(
	CurrencyDBConverter::class
)
abstract class AppDatabase : RoomDatabase() {
	abstract val exchangeRateDao: ExchangeRateDao
}
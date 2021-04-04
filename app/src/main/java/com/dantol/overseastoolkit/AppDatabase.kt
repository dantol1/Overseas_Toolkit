package com.dantol.overseastoolkit

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dantol.overseastoolkit.main.currency.database.CurrencyDBConverter
import com.dantol.overseastoolkit.main.currency.database.ExchangeRateDao
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate
import com.dantol.overseastoolkit.main.emergency.database.EmergencyInfoDao
import com.dantol.overseastoolkit.main.emergency.database.model.EmergencyInfo

@Database(
	entities = [
		ExchangeRate::class,
		EmergencyInfo::class
	],
	version = 3,
	exportSchema = false
)
@TypeConverters(
	CurrencyDBConverter::class
)
abstract class AppDatabase : RoomDatabase() {
	abstract val exchangeRateDao: ExchangeRateDao
	abstract val emergencyInfoDao: EmergencyInfoDao
}
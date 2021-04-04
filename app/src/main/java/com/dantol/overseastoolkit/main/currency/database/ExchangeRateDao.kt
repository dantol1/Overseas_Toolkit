package com.dantol.overseastoolkit.main.currency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate

@Dao
abstract class ExchangeRateDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(rate: ExchangeRate)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insertBulk(rates: List<ExchangeRate>)

	@Query("SELECT * FROM exchange_rate")
	abstract fun getExchangeRates(): List<ExchangeRate>
}
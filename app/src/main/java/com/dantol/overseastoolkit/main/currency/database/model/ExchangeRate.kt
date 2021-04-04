package com.dantol.overseastoolkit.main.currency.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRate (
	@PrimaryKey
	@ColumnInfo(name = "currency_name")
	val currency: Currency,

	@ColumnInfo(name = "to_euro_rate")
	val toEuroRate: Double,

	@ColumnInfo(name = "last_updated")
	val lastUpdated: Long?,
)
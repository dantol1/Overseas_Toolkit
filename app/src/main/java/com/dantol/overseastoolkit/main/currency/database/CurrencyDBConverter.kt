package com.dantol.overseastoolkit.main.currency.database

import androidx.room.TypeConverter
import com.dantol.overseastoolkit.main.currency.database.model.Currency


class CurrencyDBConverter {

	@TypeConverter
	fun toCurrency(name: String): Currency = Currency.valueOf(name)

	@TypeConverter
	fun fromCurrency(currency: Currency): String = currency.name
}
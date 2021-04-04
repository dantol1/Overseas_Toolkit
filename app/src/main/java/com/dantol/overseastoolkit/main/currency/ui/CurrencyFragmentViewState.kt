package com.dantol.overseastoolkit.main.currency.ui

import android.view.View
import androidx.annotation.StringRes
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate
import com.dantol.overseastoolkit.utils.Visibility

data class CurrencyFragmentViewState(

	val isLoading: Boolean = false,

	val currenciesExchangeRate: List<ExchangeRate>? = null,

	@StringRes
	val errorMessage: Int? = null,

	@Visibility
	val errorMessageVisibility: Int = View.GONE,

	val chosenFromCurrency: ExchangeRate? = null,

	@Visibility
	val chosenFromCurrencyVisibility: Int = View.GONE,

	val chosenFromCurrencyAmount: Double = 0.0,

	@Visibility
	val chosenFromCurrencyAmountVisibility: Int = View.GONE,

	val chosenToCurrency: ExchangeRate? = null,

	@Visibility
	val chosenToCurrencyVisibility: Int = View.GONE,

	val amountConverted: Double = 0.0,

	@Visibility
	val amountConvertedVisibility: Int = View.GONE,

	@Visibility
	val swapButtonVisibility: Int = View.GONE,
)

package com.dantol.overseastoolkit.main.currency.ui

sealed class CurrencyFragmentState(val viewState: CurrencyFragmentViewState?) {

	data class LoadingData(val state: CurrencyFragmentViewState?) : CurrencyFragmentState(state)

	data class ErrorLoadingData(val state: CurrencyFragmentViewState?) : CurrencyFragmentState(state)

	data class DataLoaded(val state: CurrencyFragmentViewState?) : CurrencyFragmentState(state)

	data class CurrencyConverted(val state: CurrencyFragmentViewState?) : CurrencyFragmentState(state)
}

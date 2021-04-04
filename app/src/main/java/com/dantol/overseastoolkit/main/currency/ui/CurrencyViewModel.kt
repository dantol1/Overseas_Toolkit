package com.dantol.overseastoolkit.main.currency.ui

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantol.overseastoolkit.R
import com.dantol.overseastoolkit.main.currency.database.ExchangeRateDao
import com.dantol.overseastoolkit.main.currency.database.model.Currency
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate
import com.dantol.overseastoolkit.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CurrencyViewModel @Inject constructor(
	private val exchangeRateDao: ExchangeRateDao,
	private val okHttpClient: OkHttpClient,
	private val sharedPreferences: SharedPreferences,
) : ViewModel() {

	private val currencyFragmentStateLiveData = MutableLiveData<CurrencyFragmentState>()

	val currencyFragmentState: LiveData<CurrencyFragmentState>
		get() = currencyFragmentStateLiveData

	init {
		currencyFragmentStateLiveData.postValue(
			CurrencyFragmentState.LoadingData(
				CurrencyFragmentViewState(isLoading = true)
			)
		)
	}

	fun getRates() {
		val viewState = currencyFragmentState.value?.viewState?.copy(isLoading = true) ?: CurrencyFragmentViewState(isLoading = true)
		currencyFragmentStateLiveData.postValue(CurrencyFragmentState.LoadingData(viewState))

		val lastHour = Calendar.getInstance().apply { add(Calendar.HOUR, -1) }.time
		viewModelScope.launch(Dispatchers.IO) {
			if (Date(sharedPreferences.getLong(LAST_DOWNLOADED_RATES, 0)).before(lastHour)) {
				fetchRatesFromWeb()
			} else {
				tryRestoreRatesFromDB()
			}
		}
	}

	private fun tryRestoreRatesFromDB() {
		val rates = exchangeRateDao.getExchangeRates()

		if (rates.isNotEmpty())
			showRates(rates)
		else {
			currencyFragmentStateLiveData.postValue(
				CurrencyFragmentState.ErrorLoadingData(
					currencyFragmentState.value?.viewState?.copy(
						errorMessageVisibility = View.VISIBLE,
						errorMessage = R.string.failed_to_get_rates
					)
				)
			)
		}
	}

	private fun fetchRatesFromWeb() {
		okHttpClient.newCall(
			Request.Builder()
				.url(RATES_API)
				.build()
		).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				Logger.w("failed to get rates from the web, exception - $e")
				tryRestoreRatesFromDB()
			}

			override fun onResponse(call: Call, response: Response) {
				if (response.code == HTTP_OK)
					parseResponse(response)
				else
					Logger.w("Something went wrong with fetching rates")
			}
		})
	}

	@Throws(NullPointerException::class)
	private fun parseResponse(ratesResponse: Response) {
		val responseJson = JSONObject(ratesResponse.body!!.string())
		val date = parseDate(responseJson.getString(CURRENCY_RATES_DATE_JSON_FIELD))
		val ratesByEur =
			parseRatesByEuro(responseJson.getJSONObject(CURRENCY_RATES_RATES_JSON_FIELD))
		val exchangeRates = ratesByEur.map { ratePair ->
			ExchangeRate(
				currency = Currency.valueOf(ratePair.first),
				toEuroRate = ratePair.second,
				lastUpdated = date
			)
		}

		exchangeRateDao.insertBulk(exchangeRates)
		if (date != null)
			sharedPreferences.edit().putLong(LAST_DOWNLOADED_RATES, date).apply()

		showRates(exchangeRates)
	}

	private fun showRates(exchangeRates: List<ExchangeRate>) {
		val viewState = currencyFragmentState.value?.viewState?.copy(
			isLoading = false,
			currenciesExchangeRate = exchangeRates,
			chosenToCurrency = exchangeRates[0],
			chosenToCurrencyVisibility = View.VISIBLE,
			chosenFromCurrency = exchangeRates[0],
			chosenFromCurrencyVisibility = View.VISIBLE,
			chosenFromCurrencyAmountVisibility = View.VISIBLE,
			amountConvertedVisibility = View.VISIBLE,
			swapButtonVisibility = View.VISIBLE
		)
		currencyFragmentStateLiveData.postValue(
			CurrencyFragmentState.DataLoaded(viewState)
		)
	}

	private fun parseRatesByEuro(ratesJson: JSONObject): List<Pair<String, Double>> {
		val ratesByEuro = ArrayList<Pair<String, Double>>().apply {
			add(Pair("EUR", 1.0))
		}

		for (rate in ratesJson.keys()) {
			ratesByEuro.add(Pair(rate, ratesJson.getDouble(rate)))
		}

		return ratesByEuro
	}

	private fun parseDate(dateString: String): Long? {
		val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
		return simpleDateFormat.parse(dateString)?.time
	}

	fun uiChanged(amountString: String?, fromCurrency: ExchangeRate?, toCurrency: ExchangeRate?) {
		Logger.i("amount is $amountString fromCurrency is $fromCurrency toCurrency is $toCurrency")
		if (amountString != null && fromCurrency != null && toCurrency != null) {
			try {
				val amount = amountString.toDouble()
				val exchanged = amount * toCurrency.toEuroRate * (1 / fromCurrency.toEuroRate)
				val viewState =
					currencyFragmentState.value?.viewState?.copy(amountConverted = exchanged)

				currencyFragmentStateLiveData.postValue(
					CurrencyFragmentState.CurrencyConverted(viewState)
				)
			} catch (e: NumberFormatException) {
				Logger.w("Failed to parse number, from $amountString")
			}
		}
	}

	companion object {
		private const val HTTP_OK = 200
		private const val RATES_API = "https://api.ratesapi.io/api/latest"
		private const val LAST_DOWNLOADED_RATES = "last_downloaded_rates"
		private const val CURRENCY_RATES_DATE_JSON_FIELD = "date"
		private const val CURRENCY_RATES_RATES_JSON_FIELD = "rates"
	}
}
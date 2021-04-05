package com.dantol.overseastoolkit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dantol.overseastoolkit.main.currency.database.ExchangeRateDao
import com.dantol.overseastoolkit.main.currency.database.model.Currency
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate
import com.dantol.overseastoolkit.main.currency.ui.CurrencyViewModel
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class CurrencyConversionInstrumentedTest {

	private lateinit var contextMock: Context
	private lateinit var db: AppDatabase
	private lateinit var exchangeRateDaoMock: ExchangeRateDao
	private lateinit var okHttpClient: OkHttpClient
	private lateinit var conversionViewModel: CurrencyViewModel


	@Before
	fun setup() {
		contextMock = ApplicationProvider.getApplicationContext()
		db = Room.inMemoryDatabaseBuilder(contextMock, AppDatabase::class.java).build()
		exchangeRateDaoMock = db.exchangeRateDao
		okHttpClient = OkHttpClient()
		conversionViewModel = CurrencyViewModel(
			okHttpClient = okHttpClient,
			exchangeRateDao = exchangeRateDaoMock,
			sharedPreferences = contextMock.getSharedPreferences(
				OverseasApplication.SHARED_PREFERENCE_KEY,
				Context.MODE_PRIVATE
			)
		)
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun conversionToEur_isCorrect() {
		assertEquals(
            0.25,
            conversionViewModel.convertCurrency(
                amount = 1.0,
                fromCurrency = ExchangeRate(
                    currency = Currency.ILS,
                    toEuroRate = 4.0,
                    lastUpdated = null
                ),
                toCurrency = ExchangeRate(
                    currency = Currency.EUR,
                    toEuroRate = 1.0,
                    lastUpdated = null
                )
            ),
            ACCEPTABLE_DELTA
        )
	}

	@Test
	fun conversionFromEur_isCorrect() {
		assertEquals(
            4.0,
            conversionViewModel.convertCurrency(
                amount = 1.0,
                fromCurrency = ExchangeRate(
                    currency = Currency.EUR,
                    toEuroRate = 1.0,
                    lastUpdated = null
                ),
                toCurrency = ExchangeRate(
                    currency = Currency.ILS,
                    toEuroRate = 4.0,
                    lastUpdated = null
                )
            ),
            ACCEPTABLE_DELTA
        )
	}

	@Test
	fun conversion_isCorrect() {
		assertEquals(
            0.75,
            conversionViewModel.convertCurrency(
                amount = 6.0,
                fromCurrency = ExchangeRate(
                    currency = Currency.ILS,
                    toEuroRate = 4.0,
                    lastUpdated = null
                ),
                toCurrency = ExchangeRate(
                    currency = Currency.GBP,
                    toEuroRate = 0.5,
                    lastUpdated = null
                )
            ),
            ACCEPTABLE_DELTA
        )
	}

	companion object {
		const val ACCEPTABLE_DELTA = 0.01
	}
}
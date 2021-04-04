package com.dantol.overseastoolkit.main.currency.database.model

import androidx.annotation.DrawableRes
import com.dantol.overseastoolkit.R

enum class Currency {
	EUR,
	GBP,
	HKD,
	IDR,
	ILS,
	DKK,
	INR,
	CHF,
	MXN,
	CZK,
	SGD,
	THB,
	HRK,
	MYR,
	NOK,
	CNY,
	BGN,
	PHP,
	SEK,
	PLN,
	ZAR,
	CAD,
	ISK,
	BRL,
	RON,
	NZD,
	TRY,
	JPY,
	RUB,
	KRW,
	USD,
	HUF,
	AUD,
	;

	@DrawableRes
	fun flag(): Int
		= when(this) {
			EUR -> R.drawable.ic_european_union
			GBP -> R.drawable.ic_united_kingdom
			HKD -> R.drawable.ic_hong_kong
			IDR -> R.drawable.ic_indonesia
			ILS -> R.drawable.ic_israel
			DKK -> R.drawable.ic_denmark
			INR -> R.drawable.ic_india
			CHF -> R.drawable.ic_liechtenstein
			MXN -> R.drawable.ic_mexico
			CZK -> R.drawable.ic_czech_republic
			SGD -> R.drawable.ic_singapore
			THB -> R.drawable.ic_thailand
			HRK -> R.drawable.ic_croatia
			MYR -> R.drawable.ic_malasya
			NOK -> R.drawable.ic_norway
			CNY -> R.drawable.ic_china
			BGN -> R.drawable.ic_bulgaria
			PHP -> R.drawable.ic_philippines
			SEK -> R.drawable.ic_sweden
			PLN -> R.drawable.ic_poland
			ZAR -> R.drawable.ic_south_africa
			CAD -> R.drawable.ic_canada
			ISK -> R.drawable.ic_iceland
			BRL -> R.drawable.ic_brazil
			RON -> R.drawable.ic_romania
			NZD -> R.drawable.ic_new_zealand
			TRY -> R.drawable.ic_turkey
			JPY -> R.drawable.ic_japan
			RUB -> R.drawable.ic_russia
			KRW -> R.drawable.ic_south_korea
			USD -> R.drawable.ic_united_states_of_america
			HUF -> R.drawable.ic_hungary
			AUD -> R.drawable.ic_australia
		}
}
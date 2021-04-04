package com.dantol.overseastoolkit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OverseasApplication: Application() {
	companion object {
		const val SHARED_PREFERENCE_KEY: String = "OverseasPreferences"
	}
}
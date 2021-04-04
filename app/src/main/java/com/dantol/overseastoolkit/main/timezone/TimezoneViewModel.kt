package com.dantol.overseastoolkit.main.timezone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimezoneViewModel @Inject constructor() : ViewModel() {

	fun getTimezones(onTimezonesLoaded: (List<TimeZone>) -> Unit) {
		viewModelScope.launch(Dispatchers.IO) {
			val timeZones = TimeZone.getAvailableIDs().map { TimeZone.getTimeZone(it) }
			withContext(Dispatchers.Main) {
				onTimezonesLoaded(timeZones)
			}
		}
	}
}
package com.dantol.overseastoolkit.main.emergency.ui

import com.dantol.overseastoolkit.main.emergency.database.model.EmergencyInfo

sealed class EmergencyInfoFragmentState {
	object FetchingData : EmergencyInfoFragmentState()
	data class ErrorFetchingData(val errorMsg: String) : EmergencyInfoFragmentState()
	data class DataLoaded(val emergencyInfoList: List<EmergencyInfo>) : EmergencyInfoFragmentState()
}

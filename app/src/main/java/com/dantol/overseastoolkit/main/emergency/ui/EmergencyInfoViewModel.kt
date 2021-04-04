package com.dantol.overseastoolkit.main.emergency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantol.overseastoolkit.main.emergency.database.EmergencyInfoDao
import com.dantol.overseastoolkit.main.emergency.database.model.EmergencyInfo
import com.dantol.overseastoolkit.utils.Logger
import com.dantol.overseastoolkit.utils.NoInfoReceivedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

@HiltViewModel
class EmergencyInfoViewModel @Inject constructor(
	private val okHttpClient: OkHttpClient,
	private val emergencyInfoDao: EmergencyInfoDao
) : ViewModel() {

	private val emergencyInfoFragmentStateLiveData = MutableLiveData<EmergencyInfoFragmentState>()
	val emergencyInfoFragmentState: LiveData<EmergencyInfoFragmentState>
		get() = emergencyInfoFragmentStateLiveData

	fun getEmergencyInfo() {
		emergencyInfoFragmentStateLiveData.postValue(EmergencyInfoFragmentState.FetchingData)

		viewModelScope.launch(Dispatchers.IO) {
			val emergencyInfoList = emergencyInfoDao.getEmergencyInfoList()

			if (emergencyInfoList.isEmpty())
				getEmergencyNumbersFromWeb()
			else
				showInfo(emergencyInfoList)
		}
	}

	private fun getEmergencyNumbersFromWeb() {
		okHttpClient.newCall(
			Request.Builder()
				.url(EMERGENCY_NUMBERS_API)
				.build()
		).enqueue(
			object : Callback {
				override fun onFailure(call: Call, e: IOException) {
					Logger.w("Failed to fetch emergency info")
					showFailure(e)
				}

				override fun onResponse(call: Call, response: Response) {
					if (response.code == HTTP_OK)
						parseResponse(response)
				}
			})
	}

	private fun parseResponse(response: Response) {
		val responseBodyJson = JSONArray(response.body!!.string())
		val emergencyInfoList = ArrayList<EmergencyInfo>()

		for (i in 0 until responseBodyJson.length())
			emergencyInfoList.add(parseEmergencyInfo(responseBodyJson.getJSONObject(i)))

		emergencyInfoDao.insertBulk(emergencyInfoList)
		showInfo(emergencyInfoList)
	}

	/**
	 * Parse the emergency info from the received JSON.
	 *
	 * JSON example:
	 * 	{
	"Country": {
	"Name": "Afghanistan",
	"ISOCode": "AF",
	"ISONumeric": "4"
	},
	"Ambulance": {
	"All": [
	null
	]
	},
	"Fire": {
	"All": [
	null
	]
	},
	"Police": {
	"All": [
	null
	]
	},
	"Dispatch": {
	"All": [
	null
	]
	},
	 *
	 *
	 * */
	private fun parseEmergencyInfo(info: JSONObject?): EmergencyInfo {
		if (info == null)
			throw NoInfoReceivedException("failed to receive info from the server")

		Logger.i("parsing $info")
		val countryInfo = info.getJSONObject(COUNTRY_JSON_FIELD)
		return EmergencyInfo(
			countryCode = countryInfo.getString(COUNTRY_CODE_JSON_FIELD),
			countryName = countryInfo.getString(COUNTRY_NAME_JSON_FIELD),
			ambulance = info.getJSONObject(AMBULANCE_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(ALL_JSON_FIELD)
				?: "null",
			fire = info.getJSONObject(FIRE_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(ALL_JSON_FIELD)
				?: "null",
			police = info.getJSONObject(POLICE_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(ALL_JSON_FIELD)
				?: "null",
			dispatch = info.getJSONObject(DISPATCH_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(ALL_JSON_FIELD)
				?: "null",
			dispatchGSM = info.getJSONObject(DISPATCH_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(GSM_JSON_FIELD)
				?: "null",
			dispatchFixed = info.getJSONObject(DISPATCH_JSON_FIELD)
				.getStringJSONArrayFirstElementOrNull(FIXED_JSON_FIELD) ?: "null",
		)
	}

	private fun showInfo(emergencyInfoList: List<EmergencyInfo>) {
		emergencyInfoFragmentStateLiveData.postValue(
			EmergencyInfoFragmentState.DataLoaded(
				emergencyInfoList
			)
		)
	}

	private fun showFailure(e: Exception) {
		emergencyInfoFragmentStateLiveData.postValue(EmergencyInfoFragmentState.ErrorFetchingData(e.toString()))
	}

	private fun JSONObject.getStringJSONArrayFirstElementOrNull(name: String): String? {
		return try {
			getJSONArray(name).getString(0)
		} catch (e: Exception) {
			Logger.e("Got exception while parsing emergency info - $e")
			null
		}
	}

	companion object {
		private const val EMERGENCY_NUMBERS_API = "http://emergencynumberapi.com/api/data/all"

		private const val COUNTRY_JSON_FIELD = "Country"
		private const val COUNTRY_CODE_JSON_FIELD = "ISOCode"
		private const val COUNTRY_NAME_JSON_FIELD = "Name"
		private const val AMBULANCE_JSON_FIELD = "Ambulance"
		private const val FIRE_JSON_FIELD = "Fire"
		private const val POLICE_JSON_FIELD = "Police"
		private const val DISPATCH_JSON_FIELD = "Dispatch"
		private const val ALL_JSON_FIELD = "All"
		private const val FIXED_JSON_FIELD = "Fixed"
		private const val GSM_JSON_FIELD = "GSM"
	}
}
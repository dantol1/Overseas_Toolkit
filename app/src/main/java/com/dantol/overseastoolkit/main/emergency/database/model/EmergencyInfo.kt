package com.dantol.overseastoolkit.main.emergency.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_info")
data class EmergencyInfo (
	@PrimaryKey
	@ColumnInfo(name = "country_code")
	val countryCode: String,

	@ColumnInfo(name = "country_name")
	val countryName: String,

	@ColumnInfo(name = "ambulance")
	val ambulance: String,

	@ColumnInfo(name = "fire")
	val fire: String,

	@ColumnInfo(name = "police")
	val police: String,

	@ColumnInfo(name = "dispatch")
	val dispatch: String,

	@ColumnInfo(name = "dispatch_gsm")
	val dispatchGSM: String,

	@ColumnInfo(name = "dispatch_fixed")
	val dispatchFixed: String,
)
package com.dantol.overseastoolkit.main.emergency.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dantol.overseastoolkit.main.emergency.database.model.EmergencyInfo

@Dao
abstract class EmergencyInfoDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(emergencyInfo: EmergencyInfo)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insertBulk(emergencyInfoList: List<EmergencyInfo>)

	@Query("SELECT * FROM emergency_info")
	abstract fun getEmergencyInfoList(): List<EmergencyInfo>
}
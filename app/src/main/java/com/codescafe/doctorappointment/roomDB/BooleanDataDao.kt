package com.codescafe.doctorappointment.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BooleanDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooleanData(booleanData: BooleanData)

    @Query("SELECT * FROM boolean_data WHERE `key` = :key LIMIT 1")
    fun getBooleanData(key: String): BooleanData?

    @Query("DELETE FROM boolean_data WHERE `key` = :key")
    fun deleteBooleanData(key: String)
}

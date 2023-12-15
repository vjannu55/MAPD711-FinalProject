package com.codescafe.doctorappointment.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boolean_data")
data class BooleanData(
    @PrimaryKey val key: String,
    val value: Boolean
)
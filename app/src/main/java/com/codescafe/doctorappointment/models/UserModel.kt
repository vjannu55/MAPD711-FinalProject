package com.codescafe.doctorappointment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long ,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var gender: String = "",
    var phoneNumber: String = "",
    var fee: String = "",
    var speciality: String = "",
    var city: String = "",
    var address: String = "",
    var type: String = "",

) : Serializable {
    constructor() : this(0, "", "", "", "", "", "","", "","", "")
}
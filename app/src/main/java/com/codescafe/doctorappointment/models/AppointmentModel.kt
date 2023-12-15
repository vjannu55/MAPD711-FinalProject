package com.codescafe.doctorappointment.models

import java.io.Serializable

class AppointmentModel : Serializable {
    var name: String? = ""
    var age:  String? = ""
    var mobileNumber: String? = ""
    var gender : String? = ""
    var description : String? = ""
    var fee : String? = ""
    var doctorId:kotlin.Long? = 0
    var selfId:kotlin.Long? = 0
    var relation:kotlin.String? = ""
    var date_time:kotlin.String? = ""
    var status:kotlin.String? = ""
    var key:kotlin.String? = ""
    var doctorTime: String? = ""
    var doctordate:kotlin.String? = ""
    var token:kotlin.String? = ""
    var confirm:kotlin.String? = ""
    var date:kotlin.String? = ""
    var time:kotlin.String? = ""

    constructor()

    constructor(
        name: String?,
        age: String?,
        mobileNumber: String?,
        gender: String?,
        description: String?,
        fee: String?,
        doctorId: Long?,
        selfId: Long?,
        relation: String?,
        date_time: String?,
        status: String?,
        key: String?,
        doctorTime: String?,
        doctordate: String?,
        token: String?,
        confirm: String?,
        date: String?,
        time: String?
    ) {
        this.name = name
        this.age = age
        this.mobileNumber = mobileNumber
        this.gender = gender
        this.description = description
        this.fee = fee
        this.doctorId = doctorId
        this.selfId = selfId
        this.relation = relation
        this.date_time = date_time
        this.status = status
        this.key = key
        this.doctorTime = doctorTime
        this.doctordate = doctordate
        this.token = token
        this.confirm = confirm
        this.date = date
        this.time = time
    }


}
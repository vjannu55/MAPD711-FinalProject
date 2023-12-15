package com.codescafe.doctorappointment.models

import java.io.Serializable

class TimeTableModel : Serializable {
    var mondaystart: String? = null
    var mondayend: String? = null
    var tuesdaystart: String? = null
    var tuesdayend: String? = null
    var wednesdaystart: String? = null
    var wednesdayend: String? = null
    var thursdaystart: String? = null
    var thursdayend: String? = null
    var fridaystart: String? = null
    var fridayend: String? = null
    var saturdaystart: String? = null
    var saturdayend: String? = null
    var sundaystart: String? = null
    var sundayend: String? = null

    constructor()

    constructor(
        mondaystart: String?,
        mondayend: String?,
        tuesdaystart: String?,
        tuesdayend: String?,
        wednesdaystart: String?,
        wednesdayend: String?,
        thursdaystart: String?,
        thursdayend: String?,
        fridaystart: String?,
        fridayend: String?,
        saturdaystart: String?,
        saturdayend: String?,
        sundaystart: String?,
        sundayend: String?
    ) {
        this.mondaystart = mondaystart
        this.mondayend = mondayend
        this.tuesdaystart = tuesdaystart
        this.tuesdayend = tuesdayend
        this.wednesdaystart = wednesdaystart
        this.wednesdayend = wednesdayend
        this.thursdaystart = thursdaystart
        this.thursdayend = thursdayend
        this.fridaystart = fridaystart
        this.fridayend = fridayend
        this.saturdaystart = saturdaystart
        this.saturdayend = saturdayend
        this.sundaystart = sundaystart
        this.sundayend = sundayend
    }


}
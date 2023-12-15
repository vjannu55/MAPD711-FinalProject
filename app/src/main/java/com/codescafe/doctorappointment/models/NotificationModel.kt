package com.codescafe.doctorappointment.models

import java.io.Serializable

class NotificationModel : Serializable {
    var title: String? = ""
    var count:  String? = ""
    var recieverID: String? = ""
    var senderID : String? = ""
    var id : String? = ""

    constructor()

    constructor(
        title: String?,
        count: String?,
        recieverID: String?,
        senderID: String?,
        id: String?
    ) {
        this.title = title
        this.count = count
        this.recieverID = recieverID
        this.senderID = senderID
        this.id = id
    }


}
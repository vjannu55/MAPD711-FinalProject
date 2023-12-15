package com.codescafe.doctorappointment.Doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.AppointmentModel
import com.codescafe.doctorappointment.models.NotificationModel
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONException
import org.json.JSONObject

class DocAppointmentView : AppCompatActivity() {

    var checkme: LinearLayout? = null
    private var tool_prev: Toolbar? = null
    private var nameOf: TextView? = null
    private var ageOf:TextView? = null
    private var gender:TextView? = null
    private var descriptionOf:TextView? = null
    private var feeOF:TextView? = null
    private var total_fee:TextView? = null
    private var datee:TextView? = null
    private var timee:TextView? = null
    private var nameDr:TextView? = null
    var appointmentModel: AppointmentModel? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    private var Cancel: Button? = null
    private var Confirm: Button? = null
    var activity : Activity? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_appointment_view)
        activity = this

        appointmentModel = intent.getSerializableExtra("model") as AppointmentModel

        tool_prev = findViewById(R.id.tool_prev)
        nameOf = findViewById(R.id.nameOf)
        ageOf = findViewById(R.id.ageOf)
        gender = findViewById(R.id.gender)
        descriptionOf = findViewById(R.id.descriptionOf)
        feeOF = findViewById(R.id.feeOF)
        total_fee = findViewById(R.id.total_fee)
        checkme = findViewById(R.id.checkme)
        datee = findViewById(R.id.datee)
        timee = findViewById(R.id.timee)
        Cancel = findViewById(R.id.Cancel)
        Confirm = findViewById(R.id.Confirm)
        nameDr = findViewById(R.id.nameDr)

        nameOf!!.text = appointmentModel!!.name
        ageOf!!.text = "Age : " + appointmentModel!!.age
        gender!!.text = appointmentModel!!.gender
        descriptionOf!!.text = appointmentModel!!.description
        feeOF!!.text = "$"+"CAD " + appointmentModel!!.fee
        total_fee!!.text = "$"+"CAD " + appointmentModel!!.fee
        datee!!.text = appointmentModel!!.date
        timee!!.text = appointmentModel!!.time

        appointmentModel!!.doctorId?.let {
            FirebaseDatabase.getInstance().getReference("Users")
                .child(it.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userModel: UserModel? = snapshot.getValue(UserModel::class.java)
                            nameDr!!.text = "" + userModel!!.name
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }


        if (appointmentModel!!.confirm == "confirmed") {
            Cancel!!.visibility = View.GONE
            Confirm!!.visibility = View.GONE
        }
        if (appointmentModel!!.confirm == "canceled") {
            Cancel!!.visibility = View.GONE
            Confirm!!.visibility = View.GONE
        }

        Cancel!!.setOnClickListener { view: View? ->
            openConfirmAppointment(
                "canceled"
            )
        }
        Confirm!!.setOnClickListener { view: View? ->
            openConfirmAppointment(
                "confirmed"
            )
        }

        var backBtn: ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }
    }

    private fun openConfirmAppointment(value: String) {
        bottomSheetDialog = activity?.let {
            BottomSheetDialog(
                it,
                com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog
            )
        }
        bottomSheetDialog!!.setContentView(R.layout.confirmm)
        val view: View = layoutInflater.inflate(R.layout.confirmm, null)
        bottomSheetDialog!!.setContentView(view)
        val deleteNow = view.findViewById<Button>(R.id.deleteNow)
        val cancelNow = view.findViewById<Button>(R.id.cancelNow)
        deleteNow.setOnClickListener { view1: View? ->

            val ref = appointmentModel!!.key?.let {
                FirebaseDatabase.getInstance().getReference("Appointments")
                    .child(it)
            }
            val hashMap =
                HashMap<String, Any>()
            hashMap["confirm"] = "" + value
            ref!!.updateChildren(hashMap)
                .addOnCompleteListener {
                    Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show()
                    val notificationModel = NotificationModel("","","","","")
                    CreateFeedbackNotification("Your Appointment", "From " + nameDr!!.text.toString() + " is "+value,  appointmentModel!!.selfId.toString())
                    notificationModel.id = ""+System.currentTimeMillis()
                    notificationModel.title = "Your Appointment"
                    notificationModel.count = "From " + nameDr!!.text.toString() + "is "+value
                    notificationModel.senderID = ""+appointmentModel!!.doctorId
                    notificationModel.recieverID = ""+appointmentModel!!.selfId
                    FirebaseDatabase.getInstance().getReference("Notification")
                        .child(""+notificationModel.id)
                        .setValue(notificationModel)

                    bottomSheetDialog!!.dismiss()
                    finish()
                }
        }
        cancelNow.setOnClickListener { view12: View? -> bottomSheetDialog!!.dismiss() }
        bottomSheetDialog!!.show()
    }

    private fun CreateFeedbackNotification(title: String, count: String, receiverId: String) {
        val data = JSONObject()
        try {
            data.put("NotificationType", "DecisionNotification")
            data.put("title", title)
            data.put("msg", "" + count)
            data.put("receiverId", receiverId)
            data.put("self", "" + appointmentModel!!.doctorId)
            notifiy(data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun notifiy(data: JSONObject?) {
        try {
            val queue = Volley.newRequestQueue(activity)
            val url = "https://fcm.googleapis.com/fcm/send"
            val notification_data = JSONObject()
            notification_data.put("data", data)
            notification_data.put("content_available", true)
            notification_data.put("to", "/topics/Message")
            val request: JsonObjectRequest = object : JsonObjectRequest(url, notification_data,
                Response.Listener<JSONObject> { response: JSONObject? -> },
                Response.ErrorListener { error: VolleyError? -> }) {
                override fun getHeaders(): Map<String, String> {
                    val api_key_header_value: String = Utils.Server_Key
                    val headers: MutableMap<String, String> = java.util.HashMap()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = api_key_header_value
                    Log.e("mytag", "header")
                    return headers
                }
            }
            queue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("mytag", "notify  Exception $e")
        }
    }

}
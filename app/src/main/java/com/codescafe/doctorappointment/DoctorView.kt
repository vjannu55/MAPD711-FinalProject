package com.codescafe.doctorappointment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.codescafe.doctorappointment.models.TimeTableModel
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class DoctorView : AppCompatActivity() {

    private var userModel: UserModel? = null
    private var nameOf: TextView? = null
    private var experience:TextView? = null
    private var specialty:TextView? = null
    var reviews:TextView? = null
    private var doctorFee:TextView? = null
    private var education:TextView? = null
    var des:TextView? = null
    private var imgProfile: CircleImageView? = null
    private var bgimage: ImageView? = null
    private var bookppointmentBtn: Button? = null
    var activity: Activity? = null

    var monStart: TextView? = null
    var monEnd: TextView? = null
    var tueStart: TextView? = null
    var tueEnd: TextView? = null
    var wedStart: TextView? = null
    var wedEnd: TextView? = null
    var thurStart: TextView? = null
    var thurEnd: TextView? = null
    var friStart: TextView? = null
    var friEnd: TextView? = null
    var satStart: TextView? = null
    var satEnd: TextView? = null
    var sunStart: TextView? = null
    var sunEnd: TextView? = null
    var location: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view)
        activity = this

        userModel = intent.getSerializableExtra("key") as UserModel

        nameOf = findViewById(R.id.nameOf)
        specialty = findViewById(R.id.specialty)
        bookppointmentBtn = findViewById(R.id.bookppointmentBtn)


        nameOf = findViewById(R.id.nameOf)
        imgProfile = findViewById(R.id.imgProfile)
        location = findViewById(R.id.location)
        specialty = findViewById(R.id.specialty)
        doctorFee = findViewById(R.id.doctorFee)
        bookppointmentBtn = findViewById(R.id.bookppointmentBtn)

        var backBtn:ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }


        monStart = findViewById(R.id.monStart)
        monEnd = findViewById(R.id.monEnd)
        tueStart = findViewById(R.id.tueStart)
        tueEnd = findViewById(R.id.tueEnd)
        wedStart = findViewById(R.id.wedStart)
        wedEnd = findViewById(R.id.wedEnd)
        thurStart = findViewById(R.id.thurStart)
        thurEnd = findViewById(R.id.thurEnd)
        friStart = findViewById(R.id.friStart)
        friEnd = findViewById(R.id.friEnd)
        satStart = findViewById(R.id.satStart)
        satEnd = findViewById(R.id.satEnd)
        sunStart = findViewById(R.id.sunStart)
        sunEnd = findViewById(R.id.sunEnd)
        bgimage = findViewById(R.id.bgImage)


        nameOf!!.text = userModel?.name
        specialty!!.text = userModel?.speciality + " Specialist"
        doctorFee!!.text = "$"+"CAD "+userModel?.fee

        if (userModel!!.address == null) {
            location!!.visibility = View.GONE
        } else {
            location!!.text = "View Location"
        }


        FirebaseDatabase.getInstance().getReference("TimeTable").child("Doctor")
            .child(userModel!!.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val doctorModel = snapshot.getValue(TimeTableModel::class.java)!!
                        if (doctorModel.mondaystart != null) {
                            monStart!!.text = doctorModel.mondaystart
                        }
                        if (doctorModel.mondayend != null) {
                            monEnd!!.text = doctorModel.mondayend
                        }
                        if (doctorModel.tuesdaystart != null) {
                            tueStart!!.text = doctorModel.tuesdaystart
                        }
                        if (doctorModel.tuesdayend != null) {
                            tueEnd!!.text = doctorModel.tuesdayend
                        }
                        if (doctorModel.wednesdaystart != null) {
                            wedStart!!.text = doctorModel.wednesdaystart
                        }
                        if (doctorModel.wednesdayend != null) {
                            wedEnd!!.text = doctorModel.wednesdayend
                        }
                        if (doctorModel.thursdaystart != null) {
                            thurStart!!.text = doctorModel.thursdaystart
                        }
                        if (doctorModel.thursdayend != null) {
                            thurEnd!!.text = doctorModel.thursdayend
                        }
                        if (doctorModel.fridaystart != null) {
                            friStart!!.text = doctorModel.fridaystart
                        }
                        if (doctorModel.fridayend != null) {
                            friEnd!!.text = doctorModel.fridayend
                        }
                        if (doctorModel.saturdaystart != null) {
                            satStart!!.text = doctorModel.saturdaystart
                        }
                        if (doctorModel.saturdaystart != null) {
                            satEnd!!.text = doctorModel.saturdayend
                        }
                        if (doctorModel.sundaystart != null) {
                            sunStart!!.text = doctorModel.sundaystart
                        }
                        if (doctorModel.sundayend != null) {
                            sunEnd!!.text = doctorModel.sundayend
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        bookppointmentBtn!!.setOnClickListener {
            val intent = Intent(activity, BookAppointment::class.java)
            intent.putExtra("model", userModel)
            startActivity(intent)
        }
        location!!.setOnClickListener {
            val address = userModel?.address
            //Toast.makeText(this,""+address,Toast.LENGTH_SHORT).show()
            if (!address.isNullOrBlank()) {
                val splitValues = address.split(":")

                if (splitValues.size >= 2) {
                    val latitude = splitValues[0].trim()
                    val longitude = splitValues[1].trim()
                    if (Utils.checkPermission(activity)) {
                        val intent = Intent(activity, ShowLocation::class.java)
                        intent.putExtra("latitude", latitude)
                        intent.putExtra("longitude", longitude)
                        intent.putExtra("name", userModel!!.name)
                        startActivity(intent)
                    } else {
                        // Request location permission if not granted
                        Utils.getpermission(activity)
                    }

                }
            } else {
                // Handle the case where userModel.address is null or blank
            }
        }
    }

}
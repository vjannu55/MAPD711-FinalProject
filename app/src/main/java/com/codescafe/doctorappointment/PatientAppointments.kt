package com.codescafe.doctorappointment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.adapters.AdapterPatientAppointments
import com.codescafe.doctorappointment.models.AppointmentModel
import com.codescafe.doctorappointment.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class PatientAppointments : AppCompatActivity() {

    private var backBtn : ImageView? = null
    var list: ArrayList<AppointmentModel> = ArrayList<AppointmentModel>()
    var userModel: UserModel? = null
    var rvAppointments: RecyclerView? = null
    var activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_appointments)
        activity = this

        userModel = intent.getSerializableExtra("model") as UserModel

        Log.e("userData", Gson().toJson(userModel))
        backBtn = findViewById(R.id.backBtn)
        rvAppointments = findViewById(R.id.rvAppointments)

        backBtn!!.setOnClickListener{ onBackPressed() }

    }
    override fun onStart() {
        super.onStart()
        LoadAppointments()
    }

    override fun onResume() {
        super.onResume()
        LoadAppointments()
    }

    private fun LoadAppointments() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Appointments")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()
                    for (ds in snapshot.children) {
                        val appointmentModel: AppointmentModel = ds.getValue(AppointmentModel::class.java)!!
                        Log.e("equals", ""+appointmentModel.selfId+"/"+userModel!!.id )
                        if (appointmentModel.selfId.toString() == userModel!!.id.toString()) {
                            list.add(appointmentModel)
                        }
                    }
                    val adapterAppointments = AdapterPatientAppointments(list)
                    rvAppointments!!.adapter = adapterAppointments
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
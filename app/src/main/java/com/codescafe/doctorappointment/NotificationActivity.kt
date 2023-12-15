package com.codescafe.doctorappointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.adapters.NotificationAdapter
import com.codescafe.doctorappointment.models.NotificationModel
import com.codescafe.doctorappointment.utils.UserManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationActivity : AppCompatActivity() {

    var backBtn : ImageView? = null
    var rvAppointments : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        backBtn = findViewById(R.id.backBtn)
        rvAppointments = findViewById(R.id.rvAppointments)

        backBtn!!.setOnClickListener{
            onBackPressed()
        }


        getNotifications()
    }

    var list: ArrayList<NotificationModel> = ArrayList<NotificationModel>()
    private fun getNotifications() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Notification")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()
                    for (ds in snapshot.children) {
                        val appointmentModel: NotificationModel = ds.getValue(NotificationModel::class.java)!!
                        if (appointmentModel.recieverID == UserManager.getUserDetails(applicationContext).id.toString()) {
                            list.add(appointmentModel)
                        }
                    }
                    val adapterAppointments = NotificationAdapter(list,applicationContext)
                    rvAppointments!!.adapter = adapterAppointments
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
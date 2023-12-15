package com.codescafe.doctorappointment.Doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.map.SelectLocation
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi


class EditDoctorProfile : AppCompatActivity() {

    var activity: Activity? = null
    var name: EditText? = null
    var email: TextView? = null
    var phoneNumber: TextView? = null
    var fee: EditText? = null
    var category: TextView? = null
    var selectAddress: TextView? = null
    var update: TextView? = null
    private lateinit var progressDialog: ProgressDialog
    private var userModel: UserModel? = null
    private  val LOCATION_PERMISSION_REQUEST_CODE = 123

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor_profile)
        activity = this

        userModel = intent.getSerializableExtra("model") as UserModel

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        phoneNumber = findViewById(R.id.phoneNumber)
        fee = findViewById(R.id.fee)
        category = findViewById(R.id.category)
        selectAddress = findViewById(R.id.selectAddress)
        update = findViewById(R.id.update)

        progressDialog = ProgressDialog(this)
        // Set optional properties
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)


        name!!.setText(userModel?.name)
        email!!.text = userModel?.email
        phoneNumber!!.text = userModel?.phoneNumber
        fee!!.setText(userModel?.fee)
        category!!.text = userModel?.speciality

        selectAddress!!.setOnClickListener {
            if (Utils.checkPermission(activity)) {
                val intent = Intent(activity, SelectLocation::class.java)
                intent.putExtra("model", userModel)
                startActivity(intent)
            } else {
                // Request location permission if not granted
                Utils.getpermission(activity)
            }
        }


        update!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (name!!.getText().toString().trim { it <= ' ' } == "") {
                name!!.setError("Required.")
                name!!.requestFocus()
            } else if (fee!!.getText().toString().trim { it <= ' ' } == "") {
                fee!!.setError("Required.")
                fee!!.requestFocus()
            } else {
                progressDialog.show()
                val hashMap =
                    HashMap<String, Any>()
                hashMap["name"] = name!!.getText().toString().trim { it <= ' ' }
                hashMap["speciality"] = category!!.getText().toString().trim { it <= ' ' }
                hashMap["address"] = selectAddress!!.getText().toString().trim { it <= ' ' }
                hashMap["fee"] = fee!!.getText().toString().trim { it <= ' ' }
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(userModel!!.id.toString()).updateChildren(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            getDetails()
                            Toast.makeText(this, "Profile Update", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        })

        getDetails()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start the activity
                    val intent = Intent(activity, SelectLocation::class.java)
                    intent.putExtra("model", userModel)
                    startActivity(intent)
                } else {

                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (Utils.Submit_Address != null) {
            if (Utils.Submit_Address.size == 3) {
                Log.e("address", ""+Gson().toJson(Utils.Submit_Address))
                selectAddress!!.text = ""+ Utils.Submit_Address[1] + " : "+ Utils.Submit_Address[2]
            }
        }
    }

    fun getDetails(){
        FirebaseDatabase.getInstance().getReference("Users").child(userModel!!.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userModel: UserModel? = snapshot.getValue(UserModel::class.java)
                        name!!.setText(userModel?.name)
                        email!!.text = userModel?.email
                        phoneNumber!!.text = userModel?.phoneNumber
                        fee!!.setText(userModel?.fee)
                        category!!.text = userModel?.speciality
                        selectAddress!!.text = userModel?.address
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Database Error", "onCancelled: " + error.message)
                }
            })
    }


}
package com.codescafe.doctorappointment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.codescafe.doctorappointment.utils.UserManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    private var backBtn : ImageView? = null
    private var tvMenuLogout : TextView? = null
    private var tvName : TextView? = null
    private var tvMobile : TextView? = null
    private var tvMeneMyOrders : TextView? = null
    var userModel : UserModel? = null
    var activity : Activity? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        activity = this

        val userManager = UserDataManager(activity as MenuActivity)
        userModel = intent.getSerializableExtra("model") as UserModel

        backBtn = findViewById(R.id.backBtn)
        tvName = findViewById(R.id.tvName)
        tvMeneMyOrders = findViewById(R.id.tvMeneMyOrders)
        tvMobile = findViewById(R.id.tvMobile)
        tvMenuLogout = findViewById(R.id.tvMenuLogout)


        tvName!!.text = userModel?.name
        tvMobile!!.text = userModel?.phoneNumber

        backBtn!!.setOnClickListener{
            onBackPressed()
        }

        tvMeneMyOrders!!.setOnClickListener{
            val intent = Intent(activity, PatientAppointments::class.java)
            intent.putExtra("model",userModel)
            startActivity(intent)
        }

        backBtn?.setOnClickListener{ onBackPressed()}
        tvMenuLogout?.setOnClickListener{
            AlertDialog.Builder(this@MenuActivity).setMessage("Are You Sure You Want To Logout...?")
                .setPositiveButton("Yes") { dialogInterface: DialogInterface?, i: Int ->
                    Toast.makeText(this, "LogOut Successfully", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                       userManager.logoutUser()
                        val userVal = UserModel()
                        UserManager.setUserDetails(userVal,activity)
                    }
                    startActivity(Intent(this,SplashScreen::class.java))
                    finishAffinity()
                }.setNegativeButton(
                    "Cancel"
                ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.show()
        }


    }
}
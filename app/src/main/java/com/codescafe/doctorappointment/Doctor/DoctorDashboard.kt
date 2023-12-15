package com.codescafe.doctorappointment.Doctor

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.codescafe.doctorappointment.Doctor.fragment.DocAppointmentFragment
import com.codescafe.doctorappointment.Doctor.fragment.DocHomeFragment
import com.codescafe.doctorappointment.Doctor.fragment.DocProfile
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DoctorDashboard : AppCompatActivity() {

    private val PERMISSION_CODE = 1001

    var bottomNavigationView: BottomNavigationView? = null
    var selectedFragment: Fragment? = null
    var activity: Activity? = null
    var tools: Toolbar? = null
    var citytxt: TextView? = null
    var circleImageView2: ImageView? = null
    var tv_name: AppCompatTextView? = null
    var userModel: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_dashboard)
        activity = this

        val userManager = UserDataManager(activity as DoctorDashboard)

        GlobalScope.launch {
            userModel = userManager.getUserLoginDetails()
            Log.e("userData", ""+ Gson().toJson(userModel))
        }

        citytxt = findViewById<TextView>(R.id.citytxt)
        circleImageView2 = findViewById<ImageView>(R.id.circleImageView2)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(nevigationSelected)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            DocHomeFragment()
        ).commit()

        checkPermissionsAvailable()


    }


    private val nevigationSelected =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> selectedFragment = DocHomeFragment()
                R.id.appoint -> selectedFragment = DocAppointmentFragment()
                R.id.prof -> selectedFragment = DocProfile()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectedFragment!!
                ).commit()
            }
            true
        }

    private fun checkPermissionsAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
                requestPermissions(
                    permissions,
                    PERMISSION_CODE
                )
            }
        }
    }

    override fun onBackPressed() {
        if (bottomNavigationView!!.selectedItemId == R.id.home) {
            super.onBackPressed()
            finish()
        } else {
            bottomNavigationView!!.selectedItemId = R.id.home
        }
    }

}
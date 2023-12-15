package com.codescafe.doctorappointment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codescafe.doctorappointment.Doctor.DoctorDashboard
import com.codescafe.doctorappointment.fragments.HomeFragment
import com.codescafe.doctorappointment.fragments.LoginFragment
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    companion object {
        lateinit var containerV: FrameLayout

        @JvmStatic
        fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(containerV.id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    var activity : Activity? = null
    val fragmentManager = supportFragmentManager
    var userModel : UserModel? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        activity = this

        containerV = findViewById(R.id.containerV)

        val userManager = UserDataManager(activity as HomeActivity)
        GlobalScope.launch {
            userModel = userManager.getUserLoginDetails()
            if (userManager.getBooleanData("login")){
                if (userModel?.type == "Doctor"){
                    startActivity(Intent(activity,DoctorDashboard::class.java))
                    finishAffinity()
                }else{
                    showFragment(fragmentManager,HomeFragment())
                }
            }else if (intent.getStringExtra("login") == "false"){
                if (userModel?.type == "Doctor"){
                    startActivity(Intent(activity,DoctorDashboard::class.java))
                    finishAffinity()
                }else{
                    showFragment(fragmentManager,HomeFragment())
                }
            }else{
                userManager.logoutUser()
                showFragment(fragmentManager,LoginFragment())
            }

        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            finish()
        } else {
            finish()
        }
    }
}
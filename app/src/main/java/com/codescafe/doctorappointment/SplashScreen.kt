package com.codescafe.doctorappointment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {

    private var activity : Activity? = null
    private val userType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        activity = this

        Handler().postDelayed({
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
        },3000)
    }
}
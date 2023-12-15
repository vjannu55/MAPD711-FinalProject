package com.codescafe.doctorappointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ShowLocation : AppCompatActivity() ,OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    var lat : String = ""
    var long : String = ""
    var doctorName : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)

        lat = intent.getStringExtra("latitude") as String
        long = intent.getStringExtra("longitude") as String
        doctorName = intent.getStringExtra("name") as String

        var backBtn: ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker at a specific location and move the camera
        val location = LatLng(lat.toDouble(), long.toDouble()) // Replace with your coordinates
        mMap.addMarker( MarkerOptions().position(location).title(doctorName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
package com.codescafe.doctorappointment.Doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.adapters.TimeSlotAdapter
import com.codescafe.doctorappointment.models.TimeTableModel
import com.codescafe.doctorappointment.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.ParseException
import java.util.Calendar
import java.util.Date

class DocTimeTableActivity : AppCompatActivity() {

    var doctorModel: UserModel? = null
    var updateDay: FloatingActionButton? = null
    var databaseReference: DatabaseReference? = null
    var backBtn: ImageView? = null


    var progressDialog: ProgressDialog? = null
    var rvMondaySlots: RecyclerView? = null
    var rvTuesdaySlots: RecyclerView? = null
    var rvWednesdaySlots: RecyclerView? = null
    var rvThursdaySlots: RecyclerView? = null
    var rvFridaySlots: RecyclerView? = null
    var rvSaturdaySlots: RecyclerView? = null
    var rvSundaySlots: RecyclerView? = null
    var activity: Activity? = null
    var monStart: TextView? = null
    var monEnd:TextView? = null
    var tueStart:TextView? = null
    var tueEnd:TextView? = null
    var wedStart:TextView? = null
    var wedEnd:TextView? = null
    var thurStart: TextView? = null
    var thurEnd:TextView? = null
    var friStart:TextView? = null
    var friEnd:TextView? = null
    var satStart:TextView? = null
    var satEnd:TextView? = null
    var sunStart:TextView? = null
    var sunEnd:TextView? = null
    var chour = 0
    var cmin:Int = 0

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_time_table)
        activity = this

        doctorModel = intent.getSerializableExtra("model") as UserModel

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Updating TimeTable")
        databaseReference = FirebaseDatabase.getInstance().getReference("TimeTable")

        backBtn = findViewById(R.id.backBtn)
        updateDay = findViewById<FloatingActionButton>(R.id.updateDay)

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
        rvMondaySlots = findViewById(R.id.rvMondaySlots)
        rvTuesdaySlots = findViewById(R.id.rvTuesdaySlots)
        rvWednesdaySlots = findViewById(R.id.rvWednesdaySlots)
        rvThursdaySlots = findViewById(R.id.rvThursdaySlots)
        rvFridaySlots = findViewById(R.id.rvFridaySlots)
        rvSaturdaySlots = findViewById(R.id.rvSaturdaySlots)
        rvSundaySlots = findViewById(R.id.rvSundaySlots)

        backBtn!!.setOnClickListener{
            onBackPressed()
        }
        monStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f = SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    monStart!!.setText(d)
                    val smonS = monStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        HashMap<String, Any>()
                    hashMap["mondaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (monEnd != null){
                                        val timeIntervals = generateTimeIntervals(monStart!!.text.toString(),monEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvMondaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        monEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    monEnd!!.setText(d)
                    val smonE = monEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        HashMap<String, Any>()
                    hashMap["mondayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (monStart != null){
                                        val timeIntervals = generateTimeIntervals(monStart!!.text.toString(),monEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvMondaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        tueStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    tueStart!!.setText(d)
                    val monstart =
                        tueStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["tuesdaystart"] = monstart
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (tueEnd != null){
                                        val timeIntervals = generateTimeIntervals(tueStart!!.text.toString(),tueEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvTuesdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        tueEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    tueEnd!!.setText(d)
                    val smonE = tueEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["tuesdayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (tueStart != null){
                                        val timeIntervals = generateTimeIntervals(tueStart!!.text.toString(),tueEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvTuesdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        wedStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    wedStart!!.setText(d)
                    val smonS = wedStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["wednesdaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (wedEnd != null){
                                        val timeIntervals = generateTimeIntervals(wedStart!!.text.toString(),wedEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvWednesdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        wedEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    wedEnd!!.setText(d)
                    val smonE = wedEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["wednesdayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (wedStart != null){
                                        val timeIntervals = generateTimeIntervals(wedStart!!.text.toString(),wedEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvWednesdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        thurStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    thurStart!!.setText(d)
                    val smonS = thurStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["thursdaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (thurEnd != null){
                                        val timeIntervals = generateTimeIntervals(thurStart!!.text.toString(),thurEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvThursdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        thurEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    thurEnd!!.setText(d)
                    val smonE = thurEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["thursdayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (thurStart != null){
                                        val timeIntervals = generateTimeIntervals(thurStart!!.text.toString(),thurEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvThursdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        friStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    friStart!!.setText(d)
                    val smonS = friStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["fridaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (friEnd != null){
                                        val timeIntervals = generateTimeIntervals(friStart!!.text.toString(),friEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvFridaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        friEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    friEnd!!.setText(d)
                    val smonE = friEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["fridayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (friStart != null){
                                        val timeIntervals = generateTimeIntervals(friStart!!.text.toString(),friEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvFridaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        satStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    satStart!!.setText(d)
                    val smonS = satStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["saturdaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (satEnd != null){
                                        val timeIntervals = generateTimeIntervals(satStart!!.text.toString(),satEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvSaturdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        satEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    satEnd!!.setText(d)
                    val smonE = satEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["saturdayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (satStart != null){
                                        val timeIntervals = generateTimeIntervals(satStart!!.text.toString(),satEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvSaturdaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        sunStart!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val d =
                        SimpleDateFormat("hh:mm a").format(date!!.time)
                    sunStart!!.setText(d)
                    val smonS = sunStart!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["sundaystart"] = smonS
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (sunEnd != null){
                                        val timeIntervals = generateTimeIntervals(sunStart!!.text.toString(),sunEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvSundaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }
        sunEnd!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            chour = calendar[Calendar.HOUR_OF_DAY]
            cmin = calendar[Calendar.MINUTE]
            //Launch
            val timePickerDialog = TimePickerDialog(
                activity,
                R.style.MyDatePickerStyle,
                { view, hourOfDay, minute ->
                    @SuppressLint("SimpleDateFormat") val f =
                        SimpleDateFormat("hh:mm")
                    var date: Date? = null
                    try {
                        date = f.parse("$hourOfDay:$minute")
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    @SuppressLint("SimpleDateFormat") val d =
                        SimpleDateFormat("hh:mm a").format(
                            date!!.time
                        )
                    sunEnd!!.setText(d)
                    val smonE = sunEnd!!.getText().toString().trim { it <= ' ' }
                    val hashMap =
                        java.util.HashMap<String, Any>()
                    hashMap["sundayend"] = smonE
                    databaseReference!!.child("Doctor")
                        .child(doctorModel!!.id.toString()).updateChildren(hashMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful){
                                    if (sunStart != null){
                                        val timeIntervals = generateTimeIntervals(sunStart!!.text.toString(),sunEnd!!.text.toString(),30)
                                        val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                        rvSundaySlots!!.adapter = adapter
                                    }
                                }
                            }
                        })
                },
                chour,
                cmin,
                false
            )
            timePickerDialog.show()
        }

        FirebaseDatabase.getInstance().getReference("TimeTable").child("Doctor")
            .child(doctorModel!!.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userModel = snapshot.getValue(TimeTableModel::class.java)!!
                        if (userModel.mondaystart != null) {
                            monStart!!.text = userModel.mondaystart
                        }
                        if (userModel.mondayend  != null) {
                            monEnd!!.text = userModel.mondayend
                            val timeIntervals = generateTimeIntervals(monStart!!.text.toString(),monEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvMondaySlots!!.adapter = adapter
                        }
                        if (userModel.tuesdaystart != null) {
                            tueStart!!.text = userModel.tuesdaystart
                        }
                        if (userModel.tuesdayend != null) {
                            tueEnd!!.text = userModel.tuesdayend
                            val timeIntervals = generateTimeIntervals(tueStart!!.text.toString(),tueEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvTuesdaySlots!!.adapter = adapter
                        }
                        if (userModel.wednesdaystart != null) {
                            wedStart!!.text = userModel.wednesdaystart

                        }
                        if (userModel.wednesdayend != null) {
                            wedEnd!!.text = userModel.wednesdayend
                            val timeIntervals = generateTimeIntervals(wedStart!!.text.toString(),wedEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvWednesdaySlots!!.adapter = adapter
                        }
                        if (userModel.thursdaystart != null) {
                            thurStart!!.text = userModel.thursdaystart
                        }
                        if (userModel.thursdayend != null) {
                            thurEnd!!.text = userModel.thursdayend
                            val timeIntervals = generateTimeIntervals(thurStart!!.text.toString(),thurEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvThursdaySlots!!.adapter = adapter
                        }
                        if (userModel.fridaystart != null) {
                            friStart!!.text = userModel.fridaystart
                        }
                        if (userModel.fridayend != null) {
                            friEnd!!.text = userModel.fridayend
                            val timeIntervals = generateTimeIntervals(friStart!!.text.toString(),friEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvFridaySlots!!.adapter = adapter
                        }
                        if (userModel.saturdaystart != null) {
                            satStart!!.text = userModel.saturdaystart

                        }
                        if (userModel.saturdayend != null) {
                            satEnd!!.text = userModel.saturdayend
                            val timeIntervals = generateTimeIntervals(satStart!!.text.toString(),satEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvSaturdaySlots!!.adapter = adapter
                        }
                        if (userModel.sundaystart != null) {
                            sunStart!!.text = userModel.saturdaystart
                        }
                        if (userModel.sundayend != null) {
                            sunEnd!!.text = userModel.sundayend
                            val timeIntervals = generateTimeIntervals(sunStart!!.text.toString(),sunEnd!!.text.toString(),30)
                            val adapter = activity?.let {
                                        TimeSlotAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                            rvSundaySlots!!.adapter = adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    fun generateTimeIntervals(startTime: String, endTime: String, intervalMinutes: Int): ArrayList<String>? {
        val timeIntervals = ArrayList<String>()

        try {
            val sdf = java.text.SimpleDateFormat("hh:mm a")
            val startDate: Date = sdf.parse(startTime)
            val endDate: Date = sdf.parse(endTime)

            val interval: Long = intervalMinutes * 60 * 1000.toLong()

            var currentTime: Long = startDate.time
            while (currentTime <= endDate.time) {
                val currentInterval = Date(currentTime)
                timeIntervals.add(sdf.format(currentInterval))
                currentTime += interval
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return timeIntervals
    }

}
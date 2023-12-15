package com.codescafe.doctorappointment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.codescafe.doctorappointment.adapters.TimeSlotSelectAdapter
import com.codescafe.doctorappointment.models.NotificationModel
import com.codescafe.doctorappointment.models.TimeTableModel
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.codescafe.doctorappointment.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookAppointment : AppCompatActivity() {

    var pa_name: EditText? = null
    var pa_phone:EditText? = null
    var pa_age:EditText? = null
    var descriptionOf:EditText? = null
    var male: RadioButton? = null
    var female:RadioButton? = null
    var other:RadioButton? = null
    var sex = ""
    var bookNow: Button? = null
    var onlineDoctorModel: UserModel? = null
    var activity: Activity? = null
    var progressDialog: ProgressDialog? = null
    var date_forAppoint: TextView? = null
    var time_forAppoint:TextView? = null
    var selectedTime:TextView? = null
    var rvMondaySlots:RecyclerView? = null

    var cyear = 0
    var cmonth:Int = 0
    var cday:Int = 0
    var chour = 0
    var cmin:Int = 0
    var dayName:String = ""
    var userModel:UserModel? = null

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        activity = this


        val userManager = UserDataManager(activity as BookAppointment)

        GlobalScope.launch {
            userModel = userManager.getUserLoginDetails()
        }

        onlineDoctorModel = intent.getSerializableExtra("model") as UserModel?
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sending request")
        window.statusBarColor = resources.getColor(R.color.colorPrimary)

        pa_name = findViewById(R.id.pa_name)
        pa_phone = findViewById(R.id.pa_phone)
        pa_age = findViewById(R.id.pa_age)
        descriptionOf = findViewById(R.id.descriptionOf)
        male = findViewById(R.id.male)
        female = findViewById(R.id.female)
        other = findViewById(R.id.other)
        bookNow = findViewById(R.id.bookNow)
        rvMondaySlots = findViewById(R.id.rvMondaySlots)
        selectedTime = findViewById(R.id.selectedTime)
        date_forAppoint = findViewById(R.id.date_forAppoint)

        var backBtn:ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }

        date_forAppoint?.setOnClickListener {
            val calendar = Calendar.getInstance()
            cyear = calendar[Calendar.YEAR]
            cmonth = calendar[Calendar.MONTH]
            cday = calendar[Calendar.DAY_OF_MONTH]

            //Launch
            val datePickerDialog = DatePickerDialog(
                activity as BookAppointment,
                OnDateSetListener { view, year, month, dayOfMonth ->
                    date_forAppoint!!.setText(
                        dayOfMonth.toString() + ":" + (month + 1) + ":" + year
                    )
                    val dateFormat = "dd:MM:yyyy"
                    if (dayOfMonth.toString().length == 1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dayName = getDayName("0"+dayOfMonth.toString() + ":" + (month + 1) + ":" + year,dateFormat)
                            println(dayName)
                            getTimeSlot()
                        }
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dayName = getDayName(dayOfMonth.toString() + ":" + (month + 1) + ":" + year,dateFormat)
                            println(dayName)
                            getTimeSlot()
                        }
                    }

                }, cyear, cmonth, cday
            )
            datePickerDialog.show()
        }

        bookNow?.setOnClickListener {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("Appointments")
            val name = pa_name!!.text.toString().trim { it <= ' ' }
            val phone = pa_phone!!.text.toString().trim { it <= ' ' }
            val age = pa_age!!.text.toString().trim { it <= ' ' }
            val des = descriptionOf!!.text.toString().trim { it <= ' ' }
            val key = databaseReference.push().key
            val calendar = Calendar.getInstance()
            @SuppressLint("SimpleDateFormat") var simpleDateFormat =
                java.text.SimpleDateFormat("MMM dd, yyyy")
            val current_date = simpleDateFormat.format(calendar.time)
            simpleDateFormat = java.text.SimpleDateFormat("hh:mm a")
            val current_time = simpleDateFormat.format(calendar.time)
            if (name == "") {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Enter Patient Name", Toast.LENGTH_SHORT).show()
            } else if (phone == "") {
                progressDialog!!.dismiss()
                Toast.makeText(activity, "Enter Patient Contact Number", Toast.LENGTH_SHORT).show()
            } else {
                val hashMap =
                    HashMap<String, Any?>()
                hashMap["name"] = name
                hashMap["mobileNumber"] = phone
                hashMap["age"] = age
                hashMap["description"] = des
                hashMap["doctorId"] = onlineDoctorModel!!.id
                hashMap["fee"] = onlineDoctorModel!!.fee
                hashMap["selfId"] = userModel!!.id
                hashMap["gender"] = sex
                hashMap["date_time"] = date_forAppoint!!.text.toString()
                    .trim { it <= ' ' } + "/" + selectedTime!!.text.toString()
                    .trim { it <= ' ' }
                hashMap["status"] = "pending"
                hashMap["confirm"] = "pending"
                hashMap["key"] = key
                assert(key != null)
                databaseReference.child(key!!).setValue(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val notificationModel = NotificationModel("","","","","")
                            CreateFeedbackNotification("New Appointment", "From " + userModel!!.name + " " ,  ""+onlineDoctorModel?.id.toString())
                            notificationModel.id = ""+System.currentTimeMillis()
                            notificationModel.title = "New Appointment"
                            notificationModel.count = "From " + userModel!!.name + " "
                            notificationModel.senderID = ""+userModel!!.id
                            notificationModel.recieverID = ""+onlineDoctorModel!!.id
                            FirebaseDatabase.getInstance().getReference("Notification")
                                .child(""+notificationModel.id)
                                .setValue(notificationModel)
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                progressDialog!!.dismiss()
                Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show()
                finish()
            }
            progressDialog!!.show()
        }
    }

    private fun getTimeSlot() {
        FirebaseDatabase.getInstance().getReference("TimeTable").child("Doctor")
            .child(onlineDoctorModel!!.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val timeTableModel = snapshot.getValue(TimeTableModel::class.java)!!
                        if (dayName  == "Monday"){
                            if (timeTableModel.mondaystart != null ) {
                                if (timeTableModel.mondayend != null){
                                    val timeIntervals = generateTimeIntervals(timeTableModel.mondaystart.toString(),timeTableModel.mondayend.toString(),30)
                                    val adapter = activity?.let {
                                        TimeSlotSelectAdapter(timeIntervals!!,
                                            it
                                        )
                                    }
                                    rvMondaySlots!!.visibility = View.VISIBLE
                                    rvMondaySlots!!.adapter = adapter
                                }

                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Tuesday"){
                            if (timeTableModel.tuesdaystart != null && timeTableModel.tuesdayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.tuesdaystart.toString(),timeTableModel.tuesdayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Wednesday"){
                            if (timeTableModel.wednesdaystart != null && timeTableModel.wednesdayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.wednesdaystart.toString(),timeTableModel.wednesdayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Thursday"){
                            if (timeTableModel.thursdaystart != null && timeTableModel.thursdayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.thursdaystart.toString(),timeTableModel.thursdayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Friday"){
                            if (timeTableModel.fridaystart != null && timeTableModel.fridayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.fridaystart.toString(),timeTableModel.fridayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Saturday"){
                            if (timeTableModel.saturdaystart != null && timeTableModel.saturdayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.saturdaystart.toString(),timeTableModel.saturdayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (dayName  == "Sunday"){
                            if (timeTableModel.sundaystart != null && timeTableModel.sundayend != null) {
                                val timeIntervals = generateTimeIntervals(timeTableModel.sundaystart.toString(),timeTableModel.sundayend.toString(),30)
                                val adapter = activity?.let {
                                    TimeSlotSelectAdapter(timeIntervals!!,
                                        it
                                    )
                                }
                                rvMondaySlots!!.visibility = View.VISIBLE
                                rvMondaySlots!!.adapter = adapter
                            }else{
                                rvMondaySlots!!.visibility = View.GONE
                                Toast.makeText(activity,"Time is Not Available That day",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayName(date: String, dateFormat: String): String {
        val localDate = LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern(dateFormat))
        val dayOfWeek = localDate.dayOfWeek
        val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayName
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

    @SuppressLint("SetTextI18n")
    fun SelectedTime(currentTimeSlot: String, adapterPosition: Int) {
        selectedTime!!.text = "Selected Time : $currentTimeSlot"
    }

    private fun CreateFeedbackNotification(title: String, count: String, receiverId: String) {
        val data = JSONObject()
        try {
            data.put("NotificationType", "AppointmentNotification")
            data.put("title", title)
            data.put("msg", "" + count)
            data.put("receiverId", receiverId)
            data.put("self", "" + userModel!!.id)
            notifiy(data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun notifiy(data: JSONObject?) {
        try {
            val queue = Volley.newRequestQueue(activity)
            val url = "https://fcm.googleapis.com/fcm/send"
            val notification_data = JSONObject()
            notification_data.put("data", data)
            notification_data.put("content_available", true)
            notification_data.put("to", "/topics/Message")
            val request: JsonObjectRequest = object : JsonObjectRequest(url, notification_data,
                Response.Listener<JSONObject> { response: JSONObject? -> },
                Response.ErrorListener { error: VolleyError? -> }) {
                override fun getHeaders(): Map<String, String> {
                    val api_key_header_value: String = Utils.Server_Key
                    val headers: MutableMap<String, String> = java.util.HashMap()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = api_key_header_value
                    Log.e("mytag", "header")
                    return headers
                }
            }
            queue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("mytag", "notify  Exception $e")
        }
    }

}
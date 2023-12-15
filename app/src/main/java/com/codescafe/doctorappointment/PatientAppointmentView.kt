package com.codescafe.doctorappointment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.codescafe.doctorappointment.models.AppointmentModel
import com.codescafe.doctorappointment.models.UserModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class PatientAppointmentView : AppCompatActivity() {

    var checkme: LinearLayout? = null
    private var tool_prev: Toolbar? = null
    private var nameOf: TextView? = null
    private var ageOf:TextView? = null
    private var gender:TextView? = null
    private var descriptionOf:TextView? = null
    private var feeOF:TextView? = null
    private var total_fee:TextView? = null
    private var datee:TextView? = null
    private var timee:TextView? = null
    private var nameDr:TextView? = null
    private var printR:ImageView? = null
    var appointmentModel: AppointmentModel? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    private var Cancel: Button? = null
    var activity: Activity? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_appointment_view)

        appointmentModel = intent.getSerializableExtra("model") as AppointmentModel?

        tool_prev = findViewById(R.id.tool_prev)
        nameOf = findViewById(R.id.nameOf)
        ageOf = findViewById(R.id.ageOf)
        gender = findViewById(R.id.gender)
        descriptionOf = findViewById(R.id.descriptionOf)
        feeOF = findViewById(R.id.feeOF)
        total_fee = findViewById(R.id.total_fee)
        checkme = findViewById(R.id.checkme)
        datee = findViewById(R.id.datee)
        timee = findViewById(R.id.timee)
        Cancel = findViewById(R.id.Cancel)
        nameDr = findViewById(R.id.nameDr)
        printR = findViewById(R.id.printR)

        nameOf!!.text == appointmentModel!!.name
        ageOf!!.text = "Age : " + appointmentModel!!.age
        gender!!.text == appointmentModel!!.gender
        descriptionOf!!.text == appointmentModel!!.description
        feeOF!!.text = "$"+"CAD " + appointmentModel!!.fee
        total_fee!!.text = "$"+"CAD " + appointmentModel!!.fee
        datee!!.text == appointmentModel!!.date
        timee!!.text == appointmentModel!!.time

        var backBtn:ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }

        appointmentModel!!.doctorId?.let {
            FirebaseDatabase.getInstance().getReference("Users")
                .child(it.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userModel: UserModel? = snapshot.getValue(UserModel::class.java)
                            nameDr!!.text = "" + userModel!!.name
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        Cancel!!.setOnClickListener { view: View? -> openConfirmAppointment() }
        if (appointmentModel?.confirm == "confirmed") {
            Cancel!!.visibility = View.GONE
        }
        if (appointmentModel?.confirm == "canceled") {
            Cancel!!.visibility = View.GONE
        }

        printR!!.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this)
            val v: View = LayoutInflater.from(this).inflate(R.layout.reciept, null)
            builder.setView(v)
            val share: LinearLayout
            val gallery: LinearLayout
            val mainLay: RelativeLayout
            val date: TextView
            val operator: TextView
            val appointmentsTime: TextView
            val refDetails: TextView
            val amount: TextView
            val statusOf: TextView
            val refundAmount: TextView
            val statusImg: ImageView
            val closeMe: ImageView
            closeMe = v.findViewById<ImageView>(R.id.closeMe)
            statusImg = v.findViewById<ImageView>(R.id.statusImg)
            statusOf = v.findViewById<TextView>(R.id.statusOf)
            gallery = v.findViewById<LinearLayout>(R.id.gallery)
            share = v.findViewById<LinearLayout>(R.id.share)
            date = v.findViewById(R.id.date)
            operator = v.findViewById<TextView>(R.id.operator)
            appointmentsTime = v.findViewById<TextView>(R.id.appointmentsTime)
            refDetails = v.findViewById<TextView>(R.id.refDetails)
            amount = v.findViewById<TextView>(R.id.amount)
            mainLay = v.findViewById<RelativeLayout>(R.id.mainLay)

            val dialog: Dialog = builder.create()

            operator.text = "" + nameDr!!.text.toString()

            val inputString = ""+appointmentModel!!.date_time
            val pattern = Pattern.compile("\\d{2}:\\d{2} [APMapm]{2}")
            val matcher = pattern.matcher(inputString)
            var onlineTime : String? = null
            if (matcher.find()){
                onlineTime = matcher.group()
            }

            val inputDateString = "11:12:2023/Selected Time : 09:30 PM"

            val formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy/'Selected Time : 'hh:mm a")
            val dateTime = LocalDateTime.parse(inputDateString, formatter)

            val outputFormatter = DateTimeFormatter.ofPattern("MM:dd:yyyy")
            val outputDate = dateTime.format(outputFormatter)

            date.text = "" + outputDate
            appointmentsTime.text = outputDate+ " " +onlineTime
            refDetails.text = ""+appointmentModel!!.name+"\n"+appointmentModel!!.mobileNumber

            amount.text = "$"+"CAD" + appointmentModel!!.fee

            share.setOnClickListener {
                shareRelativeLayout(mainLay)
                dialog.dismiss()
            }
            closeMe.setOnClickListener { dialog.dismiss() }
            gallery.setOnClickListener { dialog.dismiss() }

            dialog.show()
        })

    }

    private fun openConfirmAppointment() {
        bottomSheetDialog = BottomSheetDialog(
            activity!!,
            com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog
        )
        bottomSheetDialog!!.setContentView(R.layout.confirmm)
        val view = layoutInflater.inflate(R.layout.confirmm, null)
        bottomSheetDialog!!.setContentView(view)
        val deleteNow = view.findViewById<Button>(R.id.deleteNow)
        val cancelNow = view.findViewById<Button>(R.id.cancelNow)
        deleteNow.setOnClickListener { view1: View? ->
            val ref = appointmentModel!!.key?.let {
                FirebaseDatabase.getInstance().getReference("Appointments")
                    .child(it)
            }
            val hashMap =
                HashMap<String, Any>()
            hashMap["confirm"] = "canceled"
            ref!!.updateChildren(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Appointment Canceled", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog!!.dismiss()
                        finish()
                    }
                }
        }
        cancelNow.setOnClickListener { view12: View? -> bottomSheetDialog!!.dismiss() }
        bottomSheetDialog!!.show()
    }


    private fun shareRelativeLayout(relativeLayout: RelativeLayout) {
        relativeLayout.setBackgroundColor(Color.WHITE)
        relativeLayout.isDrawingCacheEnabled = true
        relativeLayout.buildDrawingCache()

        val imgBitmap: Bitmap? = relativeLayout.drawingCache

        val imgFolder = File(cacheDir, "images")
        var uri: Uri? = null

        try {
            imgFolder.mkdir()
            val file = File(imgFolder, "shared_img.jpeg")
            val stream = FileOutputStream(file)
            imgBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(
                applicationContext,
                "${BuildConfig.APPLICATION_ID}.provider",
                file
            )

        } catch (e: IOException) {
           e.printStackTrace()
        }

        relativeLayout.isDrawingCacheEnabled = false
        uri?.let {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/jpeg"
            shareIntent.putExtra(Intent.EXTRA_STREAM, it)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }


}
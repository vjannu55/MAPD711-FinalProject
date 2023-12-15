package com.codescafe.doctorappointment.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.codescafe.doctorappointment.HomeActivity
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var view : View? = null
    private var login : TextView? = null
    private var fullName : EditText? = null
    private var email : EditText? = null
    private var password : EditText? = null
    private var phoneNumber : EditText? = null
    private var registerNow : LinearLayout? = null
    private var patientB : RadioButton? = null
    private var doctorB : RadioButton? = null
    private var maleB : RadioButton? = null
    private var femaleB : RadioButton? = null
    private var tvSpeciality : TextView? = null
    private var tvCity : TextView? = null
    private var userModel : UserModel? = null
    private lateinit var progressDialog: ProgressDialog
    private var userType : String = ""
    private var sex : String = ""

    var Specialties = arrayOf(
        "Select Specialties",
        "Anesthesiology",
        "Dermatology",
        "Emergency medicine",
        "Family medicine",
        "Internal medicine",
        "Medical genetics",
        "Neurology",
        "Nuclear medicine",
        "Gynecology",
        "Ophthalmology",
        "Pathology",
        "Pediatrics",
        "Physical medicine",
        "Preventive medicine",
        "Psychiatry",
        "Radiation oncology",
        "Urology",
        "Surgery",
        "Allergy and immunology",
        "Diagnostic radiology",
        "Medical Specialist",
        "Orthopedic",
        "Cardiologist",
        "Pulmonologist"
    )


    val cities : Array<String> = arrayOf(
        "Edmonton",
        "Winnipeg",
        "Toronto",
        "Calgary",
        "Halifax",
        "Ottawa",
        "Quebec",
        "Vancouver",
        "Hamilton",
        "Montreal",
        "Regina",
        "Brantford",
        "Kitchener",
        "British Columbia",
        "Charlottetown",
        "Colwood",
        "Dawson Creek",
        "Saint John",
        "Brampton",
        "Cambridge",
        "Guelph",
        "Niagara Falls",
        "Coquitlam"
    )





    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_register, container, false)

        userModel = UserModel(0,"","","","","","","","","","")

        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        userType = "Patient"
        fullName = view?.findViewById(R.id.fullName);
        email = view?.findViewById(R.id.email);
        phoneNumber = view?.findViewById(R.id.phoneNumber);
        password = view?.findViewById(R.id.password);
        patientB = view?.findViewById(R.id.patientB);
        doctorB = view?.findViewById(R.id.doctorB);
        maleB = view?.findViewById(R.id.maleB);
        femaleB = view?.findViewById(R.id.femaleB);
        tvSpeciality = view?.findViewById(R.id.tvSpeciality);
        tvCity = view?.findViewById(R.id.tvCity);
        login = view?.findViewById(R.id.login);
        registerNow = view?.findViewById(R.id.registerNow);


        patientB?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userType = "Patient"
                tvCity?.visibility = View.GONE
                tvSpeciality?.visibility = View.GONE
            }
        }

        doctorB?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userType = "Doctor"
                tvCity?.visibility = View.VISIBLE
                tvSpeciality?.visibility = View.VISIBLE
            }
        }

        registerNow!!.setOnClickListener{
            if (fullName?.text.toString() == ""){
                fullName?.error = "Required."
            }else if (email?.text.toString() == ""){
                email?.error = "Required."
            }else if (phoneNumber?.text.toString() == ""){
                phoneNumber?.error = "Required."
            }else {
                if (patientB?.isChecked == true){
                    userType = "Patient"
                }else if (doctorB?.isChecked == true){
                    userType = "Doctor"
                }

                if (maleB?.isChecked == true){
                    sex = "Male"
                }else if (femaleB?.isChecked == true){
                    sex = "Female"
                }
                progressDialog.show()
                val key = System.currentTimeMillis().toLong();
                val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

                val emailQuery: Query = databaseReference.orderByChild("email").equalTo(email?.text.toString())

                emailQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()){
                            progressDialog.dismiss()
                            email?.error = "Email Already Exist"
                        }else{
                            userModel!!.id = key
                            userModel!!.name = fullName?.text.toString()
                            userModel!!.email = email?.text.toString()
                            userModel!!.password = password?.text.toString()
                            userModel!!.phoneNumber = phoneNumber?.text.toString()
                            userModel!!.gender = sex
                            if (userType == "Doctor"){
                                userModel!!.city = tvCity?.text.toString()
                                userModel!!.speciality = tvSpeciality?.text.toString()
                            }
                            userModel!!.type = userType

                            databaseReference.child(key.toString()).setValue(userModel)
                                .addOnCompleteListener(object : OnCompletionListener,
                                    OnCompleteListener<Void> {
                                    override fun onCompletion(p0: MediaPlayer?) {
                                        progressDialog.dismiss()
                                    }
                                    override fun onComplete(p0: Task<Void>) {
                                        if (p0.isSuccessful){
                                            progressDialog.dismiss()
                                            Toast.makeText(activity,"Account Created Successfully",Toast.LENGTH_LONG).show()
                                            HomeActivity.showFragment(requireActivity().supportFragmentManager,LoginFragment())
                                        }
                                    }
                                })
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        progressDialog.dismiss()
                        Toast.makeText(activity,""+databaseError.message,Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        password?.setTag("show")
        password?.setOnTouchListener(OnTouchListener setOnTouchListener@{ v: View?, event: MotionEvent ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= password!!.getRight() - password!!.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    if (password!!.getTag() == "show") {
                        password!!.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_pass,
                            0,
                            R.drawable.ic_hide,
                            0
                        )
                        password!!.setTransformationMethod(null)
                        password!!.setTag("hide")
                    } else {
                        password!!.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_pass,
                            0,
                            R.drawable.ic_show,
                            0
                        )
                        password!!.setTransformationMethod(PasswordTransformationMethod())
                        password!!.setTag("show")
                    }
                    return@setOnTouchListener true
                }
            }
            false
        })

        tvSpeciality?.setOnClickListener{
            showSpecialtiesDialog(Specialties)
        }

        tvCity?.setOnClickListener{
            showCityList(cities)
        }

        login?.setOnClickListener{
            HomeActivity.showFragment(requireActivity().supportFragmentManager,LoginFragment())
        }

        return view
    }

    private fun showSpecialtiesDialog(specialties: Array<String>) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Specialty")

        builder.setItems(specialties, DialogInterface.OnClickListener { dialog, which ->
            val selectedSpecialty = specialties[which]
            Toast.makeText(requireActivity(), "Selected Specialty: $selectedSpecialty", Toast.LENGTH_SHORT).show()
            tvSpeciality!!.text = selectedSpecialty
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun showCityList(cities: Array<String>) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select City")

        builder.setItems(cities, DialogInterface.OnClickListener { dialog, which ->
            val selectedCity = cities[which]
            tvCity!!.text = selectedCity
        })

        val dialog = builder.create()
        dialog.show()
    }
}
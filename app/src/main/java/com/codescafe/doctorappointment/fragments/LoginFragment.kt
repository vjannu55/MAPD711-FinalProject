package com.codescafe.doctorappointment.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codescafe.doctorappointment.Doctor.DoctorDashboard
import com.codescafe.doctorappointment.HomeActivity
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.codescafe.doctorappointment.utils.UserManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var view: View? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var login: LinearLayout? = null
    private var register: LinearLayout? = null
    private var rememberMe: CheckBox? = null
    private lateinit var progressDialog: ProgressDialog
    private var loggedIn : String = "true"

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_login, container, false)
        email = view?.findViewById(R.id.email)
        password = view?.findViewById(R.id.password)
        register = view?.findViewById(R.id.register)
        login = view?.findViewById(R.id.login)
        rememberMe = view?.findViewById(R.id.rememberMe)

        progressDialog = ProgressDialog(activity)
        // Set optional properties
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        login!!.setOnClickListener { v ->
            val mEmail = email!!.text.toString().trim { it <= ' ' }
            val mPassword: String = password!!.text.toString()
            if (mEmail.isEmpty()) {
                Snackbar.make(
                    v,
                    "Enter your email",
                    Snackbar.LENGTH_LONG
                ).show()

            } else if (mPassword.isEmpty()) {
                Snackbar.make(v, "Enter your password", Snackbar.LENGTH_LONG).show()

            } else {
                progressDialog.show()

                FirebaseDatabase.getInstance().getReference("Users")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) =
                            if (snapshot.exists()) {
                                for (dataSnapshot : DataSnapshot  in snapshot.children) {
                                    val mEmail = dataSnapshot.child("email").getValue(String::class.java)
                                    val pass = dataSnapshot.child("password").getValue(String::class.java)

                                    if (mEmail != null && pass != null) {
                                        if (mEmail == email!!.text.toString() && pass == password!!.text.toString()){
                                            val id = dataSnapshot.child("id").getValue(Long::class.java)
                                            var vaModel: UserModel? = null
                                            vaModel = dataSnapshot.getValue(UserModel::class.java)
                                            vaModel?.id = id!!.toLong()
                                            val userManager = UserDataManager(requireContext())
                                            loggedIn = "false"
                                            if (vaModel != null) {
                                                if (!rememberMe!!.isChecked){
                                                    GlobalScope.launch {
                                                        Log.e("userData", ""+Gson().toJson(vaModel))
                                                        userManager.run {
                                                            setUserDetails(vaModel)
                                                        }

                                                        if (vaModel.type == "Doctor") {
                                                            progressDialog.dismiss()
                                                            startActivity(Intent(requireActivity(), DoctorDashboard::class.java)
                                                                .putExtra("login","false"))
                                                            requireActivity().finish()
                                                        } else {
                                                            progressDialog.dismiss()
                                                            startActivity(Intent(requireActivity(), HomeActivity::class.java)
                                                                .putExtra("login","false"))
                                                            requireActivity().finish()
                                                        }
                                                    }
                                                    progressDialog.dismiss()
                                                }else{
                                                    GlobalScope.launch {
                                                        Log.e("userData", ""+Gson().toJson(vaModel))
                                                        userManager.run {
                                                            setUserDetails(vaModel)
                                                            UserManager.setUserDetails(vaModel,activity)
                                                            setBooleanData("login", true)
                                                        }

                                                        if (vaModel.type == "Doctor") {
                                                            progressDialog.dismiss()
                                                            startActivity(Intent(requireActivity(), DoctorDashboard::class.java)
                                                                .putExtra("login","true"))
                                                            requireActivity().finish()
                                                        } else {
                                                            progressDialog.dismiss()
                                                            startActivity(Intent(requireActivity(), HomeActivity::class.java)
                                                                .putExtra("login","true"))
                                                            requireActivity().finish()
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                if (loggedIn == "true"){
                                    progressDialog.dismiss()
                                    Toast.makeText(activity,"Credentials Invalid", Toast.LENGTH_LONG).show()
                                }else{

                                }
                            } else{

                            }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(activity,""+error.message, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
        register!!.setOnClickListener{
            HomeActivity.showFragment(requireActivity().supportFragmentManager,RegisterFragment())
        }
        password?.setTag("show")
        password?.setOnTouchListener(View.OnTouchListener setOnTouchListener@{ v: View?, event: MotionEvent ->
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

        return view
    }

}
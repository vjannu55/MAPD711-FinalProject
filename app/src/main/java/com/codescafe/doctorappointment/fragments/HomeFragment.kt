package com.codescafe.doctorappointment.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.codescafe.doctorappointment.DoctorsActivity
import com.codescafe.doctorappointment.HomeActivity
import com.codescafe.doctorappointment.MenuActivity
import com.codescafe.doctorappointment.NotificationActivity
import com.codescafe.doctorappointment.PatientAppointments
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.codescafe.doctorappointment.utils.UserManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        var userModel : UserModel? = null

        @JvmStatic
        fun updateUser(activity : Activity) {
            FirebaseDatabase.getInstance().getReference("Users").child(userModel!!.id.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userModel: UserModel? = snapshot.getValue(UserModel::class.java)
                            UserManager.setUserDetails(userModel,activity)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Database Error", "onCancelled: " + error.message)
                    }
                })
        }
    }
    private var view : View? = null
    private var MenuTo : ImageView? = null
    private var notificationTo : ImageView? = null
    private var toDocs : CardView? = null
    private var toAppointment : CardView? = null
    private var userModel : UserModel? = null


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view  =  inflater.inflate(R.layout.fragment_home, container, false)

        val userManager = UserDataManager(activity as HomeActivity)

        GlobalScope.launch {
            userModel = userManager.getUserLoginDetails()
        }

        MenuTo = view?.findViewById(R.id.MenuTo)
        toDocs = view?.findViewById(R.id.toDocs)
        notificationTo = view?.findViewById(R.id.notificationTo)
        toAppointment = view?.findViewById(R.id.toAppointment)


        toDocs?.setOnClickListener{
            val intent = Intent(requireActivity(), DoctorsActivity::class.java)
            startActivity(intent)
        }
        toAppointment?.setOnClickListener{
            val intent = Intent(requireActivity(), PatientAppointments::class.java)
            intent.putExtra("model",userModel)
            startActivity(intent)
        }

        MenuTo?.setOnClickListener{
            val intent = Intent(requireActivity(), MenuActivity::class.java)
            intent.putExtra("model",userModel)
            startActivity(intent)
        }
        notificationTo?.setOnClickListener{
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            intent.putExtra("model",userModel)
            startActivity(intent)
        }


        return view;
    }




}
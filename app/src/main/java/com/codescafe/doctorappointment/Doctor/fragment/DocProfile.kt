package com.codescafe.doctorappointment.Doctor.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codescafe.doctorappointment.Doctor.DocTimeTableActivity
import com.codescafe.doctorappointment.Doctor.EditDoctorProfile
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.SplashScreen
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.roomDB.UserDataManager
import com.codescafe.doctorappointment.utils.UserManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DocProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocProfile : Fragment() {
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
         * @return A new instance of fragment DocProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DocProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var v: View? = null
    private var logout: RelativeLayout? = null
    private var timeTableTo:RelativeLayout? = null
    private var personPicture: CircleImageView? = null
    private var noName: TextView? = null
    private var noNumber:TextView? = null
    private var userModel: UserModel? = null
    private var editTo: ImageView? = null

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_doc_profile, container, false)

        val userManager = UserDataManager(requireContext())

        GlobalScope.launch {
            userModel = userManager.getUserLoginDetails()
        }

        noName = v?.findViewById(R.id.noName)
        noNumber = v?.findViewById(R.id.noNumber)
        timeTableTo = v?.findViewById(R.id.timeTableTo)
        logout = v?.findViewById(R.id.logout)
        personPicture = v?.findViewById(R.id.personPicture)
        editTo = v?.findViewById(R.id.editTo)


        Glide.with(requireActivity()).load(resources.getDrawable(R.drawable.placeholder_user))
        noName!!.text = userModel?.name
        noNumber!!.text = userModel?.phoneNumber


        timeTableTo!!.setOnClickListener {
            startActivity(
                Intent(requireActivity(), DocTimeTableActivity::class.java)
                    .putExtra("model",userModel)
            )
        }
        editTo!!.setOnClickListener {
            startActivity(
                Intent(requireActivity(), EditDoctorProfile::class.java)
                    .putExtra("model",userModel)
            )
        }

        logout!!.setOnClickListener {
            AlertDialog.Builder(requireActivity()).setMessage("Are You Sure You Want To Logout...?")
                .setPositiveButton("Yes") { dialogInterface: DialogInterface?, i: Int ->
                    Toast.makeText(requireActivity(), "LogOut Successfully", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        userManager.logoutUser()
                        val userVal = UserModel()
                        UserManager.setUserDetails(userVal,requireActivity())
                    }
                    startActivity(Intent(requireActivity(), SplashScreen::class.java))
                    requireActivity().finishAffinity()
                }.setNegativeButton(
                    "Cancel"
                ) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }.show()
        }
        return v
    }


}
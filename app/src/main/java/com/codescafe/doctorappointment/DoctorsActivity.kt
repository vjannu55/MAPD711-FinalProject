package com.codescafe.doctorappointment

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.adapters.DocAdapter
import com.codescafe.doctorappointment.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class DoctorsActivity : AppCompatActivity() {

    var rvDocs: RecyclerView? = null
    var activity: Activity? = null
    var type: String? = null
    var password: EditText? = null
    var array_sort: ArrayList<UserModel> = ArrayList<UserModel>()
    var list: ArrayList<UserModel> = ArrayList<UserModel>()
    var databaseReference: DatabaseReference? = null
    var textlength = 0
    var cityText: TextView? = null
    var selectedCity = "All"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)
        activity = this

        cityText = findViewById(R.id.cityText)
        rvDocs = findViewById(R.id.rvDocs)

        var backBtn: ImageView? = null
        backBtn = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            onBackPressed()
        }

        type = intent.getStringExtra("Title")
        password = findViewById(R.id.password)
        password?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = password!!.getText().length
                array_sort.clear()
                for (i in list.indices) {
                    if (textlength <= list[i]?.name?.length!!) {
                        list[i]?.name?.toLowerCase()?.trim()?.let { Log.d("ertyyy", it) }
                        if (list[i]?.name?.toLowerCase()?.trim()?.contains(
                                password!!.text.toString().lowercase(Locale.getDefault())
                                    .trim { it <= ' ' }) == true
                        ) {
                            list[i].let { array_sort.add(it) }
                        }
                    }
                }
                docAdapter = DocAdapter(array_sort)
                rvDocs!!.setAdapter(docAdapter)
            }
        })
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val autoComplete = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (suggestionSnapshot in dataSnapshot.children) {
                    val type = dataSnapshot.child("type").getValue(String::class.java)
                    if (type == "Doctor"){
                        val suggestion = suggestionSnapshot.child("name").getValue(
                            String::class.java
                        )
                        autoComplete.add(suggestion)
                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        cityText?.setOnClickListener(View.OnClickListener {
            showCityList(cities)
        })
        getDocs(selectedCity)
    }



    var docAdapter: DocAdapter? = null
    private fun getDocs(city: String) {
        FirebaseDatabase.getInstance().getReference("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list.clear()
                        for (ds in snapshot.children) {
                            val doc_model: UserModel = ds.getValue(UserModel::class.java)!!
                            if (doc_model.type == "Doctor"){
                                if (city == "All"){
                                    list.add(doc_model)
                                }else if (doc_model.city.trim() == city) {
                                    list.add(doc_model)
                                }
                            }
                        }
                        if (list.size > 0) {
                            docAdapter = DocAdapter(list)
                            rvDocs!!.adapter = docAdapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showCityList(cities: Array<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select City")

        builder.setItems(cities, DialogInterface.OnClickListener { dialog, which ->
            val selectedCity = cities[which]
            cityText!!.text = selectedCity
            getDocs(selectedCity)
        })

        val dialog = builder.create()
        dialog.show()
    }
}

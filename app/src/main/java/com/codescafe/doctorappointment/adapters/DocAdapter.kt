package com.codescafe.doctorappointment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.DoctorView
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel

class DocAdapter(private val itemList: ArrayList<UserModel>) :
    RecyclerView.Adapter<DocAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOF: TextView = itemView.findViewById(R.id.nameOF)
        val cityOf: TextView = itemView.findViewById(R.id.cityOf)
        val specialityOfEngilsh: TextView = itemView.findViewById(R.id.specialityOfEngilsh)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dcotors, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : UserModel = itemList[position]
        holder.nameOF.text = item.name
        holder.cityOf.text = item.city
        holder.specialityOfEngilsh.text = item.speciality

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DoctorView::class.java)
            // Pass any data to the next activity if needed
            intent.putExtra("key", item)  // Replace "key" and someData with your actual data
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}



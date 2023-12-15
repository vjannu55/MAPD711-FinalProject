package com.codescafe.doctorappointment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.Doctor.DocAppointmentView
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.AppointmentModel

class AdapterAppointments(private val itemList: ArrayList<AppointmentModel?>) :
    RecyclerView.Adapter<AdapterAppointments.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : AppointmentModel? = itemList[position]
        holder.name.text = item?.name
        holder.status.text = item?.confirm

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DocAppointmentView::class.java)
            // Pass any data to the next activity if needed
            intent.putExtra("model", item)  // Replace "key" and someData with your actual data
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

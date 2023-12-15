package com.codescafe.doctorappointment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.R


class TimeSlotAdapter(private val timeSlots: ArrayList<String>,private val context: Context) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {


    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.slot_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val currentTimeSlot = timeSlots[position]
        holder.timeTextView.text = currentTimeSlot

    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }
}
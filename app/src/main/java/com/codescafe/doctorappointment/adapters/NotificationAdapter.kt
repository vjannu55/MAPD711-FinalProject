package com.codescafe.doctorappointment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.NotificationModel


class NotificationAdapter(private val timeSlots: ArrayList<NotificationModel>, private val context: Context) : RecyclerView.Adapter<NotificationAdapter.TimeSlotViewHolder>() {


    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val tv_msg: TextView = itemView.findViewById(R.id.tv_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return TimeSlotViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val notificationModel = timeSlots[position]

        holder.tv_title.text = notificationModel.title
        holder.tv_msg.text = notificationModel.count


    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }
}
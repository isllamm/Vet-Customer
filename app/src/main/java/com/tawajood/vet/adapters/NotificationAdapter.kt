package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawajood.vet.databinding.ItemNotificationBinding
import com.tawajood.vet.pojo.NotificationData


class NotificationAdapter :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notifications = mutableListOf<NotificationData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationAdapter.NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {

        holder.binding.body.text = notifications[position].body
        holder.binding.date.text = notifications[position].date
        holder.binding.time.text = notifications[position].time

    }

    override fun getItemCount(): Int {
        return notifications.size
    }

}
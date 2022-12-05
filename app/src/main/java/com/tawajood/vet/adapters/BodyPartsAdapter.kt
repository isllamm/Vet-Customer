package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawajood.vet.databinding.ItemBodyPartsStatusBinding
import com.tawajood.vet.databinding.ItemNotificationBinding
import com.tawajood.vet.pojo.BodyPart
import com.tawajood.vet.pojo.NotificationData

class BodyPartsAdapter :
    RecyclerView.Adapter<BodyPartsAdapter.BodyPartsViewHolder>() {

    var bodyParts = mutableListOf<BodyPart>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class BodyPartsViewHolder(val binding: ItemBodyPartsStatusBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BodyPartsAdapter.BodyPartsViewHolder {
        val binding =
            ItemBodyPartsStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BodyPartsAdapter.BodyPartsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BodyPartsAdapter.BodyPartsViewHolder,
        position: Int
    ) {

        holder.binding.tvE.text = bodyParts[position].name
        holder.binding.tvStatusE.text = bodyParts[position].status.toString()


    }

    override fun getItemCount(): Int {
        return bodyParts.size
    }

}
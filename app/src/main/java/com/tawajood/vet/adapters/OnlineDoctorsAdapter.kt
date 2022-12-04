package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemDoctorBinding
import com.tawajood.vet.pojo.Clinic

class OnlineDoctorsAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<OnlineDoctorsAdapter.OnlineDoctorsViewHolder>() {

    var doctors = mutableListOf<Clinic>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class OnlineDoctorsViewHolder(val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnlineDoctorsAdapter.OnlineDoctorsViewHolder {
        val binding =
            ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnlineDoctorsAdapter.OnlineDoctorsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OnlineDoctorsAdapter.OnlineDoctorsViewHolder,
        position: Int
    ) {
        if (doctors[position].status_online == 1) {
            Glide.with(holder.itemView.context).load(R.drawable.ic_online)
                .into(holder.binding.status)
        }
        holder.binding.name.text = doctors[position].name
        Glide.with(holder.itemView.context).load(doctors[position].image)
            .into(holder.binding.img)

        holder.binding.fees.text = doctors[position].consultation_fees.toString()
        holder.binding.time.text = doctors[position].consultation_duration
        holder.binding.rate.text = doctors[position].rate.toString()

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
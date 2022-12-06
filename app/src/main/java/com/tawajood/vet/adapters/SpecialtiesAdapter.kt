package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemSpecialtiesBinding
import com.tawajood.vet.pojo.Specialties
import com.tawajood.vet.pojo.SpecialtiesName

class SpecialtiesAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<SpecialtiesAdapter.SpecialtiesViewHolder>() {

    var specialties = mutableListOf<SpecialtiesName>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class SpecialtiesViewHolder(val binding: ItemSpecialtiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecialtiesAdapter.SpecialtiesViewHolder {
        val binding =
            ItemSpecialtiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialtiesAdapter.SpecialtiesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SpecialtiesAdapter.SpecialtiesViewHolder,
        position: Int
    ) {
        holder.binding.name.text = specialties[position].specialization.name
        Glide.with(holder.itemView.context)
            .load("https://vet.horizon.net.sa/uploads/specializations_images/" + specialties[position].specialization.image)
            .into(holder.binding.img)

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return specialties.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
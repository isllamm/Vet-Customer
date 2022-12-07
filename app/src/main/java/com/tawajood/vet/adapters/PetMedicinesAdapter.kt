package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemMedicineBinding
import com.tawajood.vet.databinding.ItemVaccinationBinding
import com.tawajood.vet.pojo.Consultant

class PetMedicinesAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<PetMedicinesAdapter.PetMedicinesViewHolder>() {

    var consultant = mutableListOf<Consultant>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PetMedicinesViewHolder(val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetMedicinesAdapter.PetMedicinesViewHolder {
        val binding =
            ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetMedicinesAdapter.PetMedicinesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetMedicinesAdapter.PetMedicinesViewHolder,
        position: Int
    ) {
        holder.binding.name.text = consultant[position].clinic.name
        holder.binding.date.text = consultant[position].created_at.substring(0, 10)
        Glide.with(holder.itemView.context).load(consultant[position].clinic.image)
            .into(holder.binding.img)

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return consultant.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
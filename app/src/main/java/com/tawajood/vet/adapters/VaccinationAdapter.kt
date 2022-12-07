package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawajood.vet.databinding.ItemVaccinationBinding
import com.tawajood.vet.pojo.Vaccinations

class VaccinationAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder>() {

    var vaccinations = mutableListOf<Vaccinations>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class VaccinationViewHolder(val binding: ItemVaccinationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VaccinationAdapter.VaccinationViewHolder {
        val binding =
            ItemVaccinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VaccinationAdapter.VaccinationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VaccinationAdapter.VaccinationViewHolder,
        position: Int
    ) {
        holder.binding.name.text = vaccinations[position].vaccinationType
        holder.binding.date.text = vaccinations[position].date

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return vaccinations.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
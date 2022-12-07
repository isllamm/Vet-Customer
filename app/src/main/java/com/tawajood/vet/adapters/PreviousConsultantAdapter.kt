package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemPreviousConsultantBinding
import com.tawajood.vet.databinding.ItemVaccinationBinding
import com.tawajood.vet.pojo.Consultant
import com.tawajood.vet.pojo.Vaccinations

class PreviousConsultantAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<PreviousConsultantAdapter.PreviousConsultantViewHolder>() {

    var consultant = mutableListOf<Consultant>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PreviousConsultantViewHolder(val binding: ItemPreviousConsultantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviousConsultantAdapter.PreviousConsultantViewHolder {
        val binding =
            ItemPreviousConsultantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PreviousConsultantAdapter.PreviousConsultantViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PreviousConsultantAdapter.PreviousConsultantViewHolder,
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
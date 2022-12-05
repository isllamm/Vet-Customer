package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawajood.vet.databinding.ItemConsultantBinding
import com.tawajood.vet.pojo.Consultant

class PreviousConsultAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<PreviousConsultAdapter.PreviousConsultViewHolder>() {

    var consultants = mutableListOf<Consultant>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PreviousConsultViewHolder(val binding: ItemConsultantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviousConsultAdapter.PreviousConsultViewHolder {
        val binding =
            ItemConsultantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviousConsultAdapter.PreviousConsultViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PreviousConsultAdapter.PreviousConsultViewHolder,
        position: Int
    ) {
        holder.binding.date.text = consultants[position].created_at.substring(0, 10)
        holder.binding.type.text = consultants[position].requestType.name
        holder.binding.number.text = consultants[position].id.toString()
        holder.binding.name.text = consultants[position].clinic.name

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return consultants.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
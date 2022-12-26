package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemPetBinding
import com.tawajood.vet.pojo.Pet

class SelectPetAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<SelectPetAdapter.SelectPetViewHolder>() {

    var myPets = mutableListOf<Pet>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSelected(position: Int) {
        myPets.forEach {
            it.isSelected = false
        }
        myPets[position].isSelected = true
        notifyDataSetChanged()
    }

    class SelectPetViewHolder(val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectPetAdapter.SelectPetViewHolder {
        val binding =
            ItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectPetAdapter.SelectPetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SelectPetAdapter.SelectPetViewHolder,
        position: Int
    ) {
        Glide.with(holder.itemView.context).load(myPets[position].image).into(holder.binding.img)
        holder.binding.name.text = myPets[position].name
        holder.binding.type.isVisible = false

        if (myPets[position].isSelected) {
            holder.binding.root.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.stroke_border_radius_12_light_blue
            )
        } else {
            holder.binding.root.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.stroke_border_radius_12_none
            )
        }
        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
            setSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return myPets.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemDoctorBinding
import com.tawajood.vet.databinding.ItemPetBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.pojo.Pet

class MyPetsAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<MyPetsAdapter.MyPetsViewHolder>() {

    var myPets = mutableListOf<Pet>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyPetsViewHolder(val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPetsAdapter.MyPetsViewHolder {
        val binding =
            ItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPetsAdapter.MyPetsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyPetsAdapter.MyPetsViewHolder,
        position: Int
    ) {
        Glide.with(holder.itemView.context).load(myPets[position].image).into(holder.binding.img)
        holder.binding.name.text = myPets[position].name
        holder.binding.type.text = myPets[position].type

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return myPets.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemVendorBinding

import com.tawajood.vet.pojo.Vendor

class VendorsAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<VendorsAdapter.VendorsViewHolder>() {
    var vendors = mutableListOf<Vendor>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class VendorsViewHolder(val binding: ItemVendorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VendorsAdapter.VendorsViewHolder {
        val binding =
            ItemVendorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorsAdapter.VendorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorsAdapter.VendorsViewHolder, position: Int) {

        holder.binding.name.text = vendors[position].username

        Glide.with(holder.itemView.context).load(vendors[position].image)
            .into(holder.binding.img)


        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return vendors.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
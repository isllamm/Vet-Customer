package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemCartBinding
import com.tawajood.vet.pojo.Cart

class ConfirmOrderAdapter() :
    RecyclerView.Adapter<ConfirmOrderAdapter.ConfirmOrderViewHolder>() {
    var carts = mutableListOf<Cart>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class ConfirmOrderViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConfirmOrderAdapter.ConfirmOrderViewHolder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConfirmOrderAdapter.ConfirmOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ConfirmOrderAdapter.ConfirmOrderViewHolder,
        position: Int
    ) {
        holder.binding.delete.visibility = View.GONE
        Glide.with(holder.itemView.context).load(carts[position].image).into(holder.binding.img)
        holder.binding.name.text = carts[position].product_name
        holder.binding.price.text = carts[position].price + R.string.Rs
        holder.binding.quantity.text = carts[position].quantity.toString()



    }

    override fun getItemCount(): Int {
        return carts.size
    }


}
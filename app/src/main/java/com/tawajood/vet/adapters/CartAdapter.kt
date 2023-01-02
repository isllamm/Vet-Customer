package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemCartBinding
import com.tawajood.vet.databinding.ItemProductBinding
import com.tawajood.vet.pojo.Cart
import com.tawajood.vet.pojo.Product

class CartAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    var carts = mutableListOf<Cart>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.CartViewHolder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartAdapter.CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(carts[position].image).into(holder.binding.img)
        holder.binding.name.text = carts[position].product_name
        holder.binding.price.text = carts[position].price + R.string.Rs
        holder.binding.quantity.text = carts[position].quantity.toString()

        holder.binding.delete.setOnClickListener {
            onItemClick.onDeleteClicked(position)
        }

    }

    override fun getItemCount(): Int {
        return carts.size
    }

    interface OnItemClick {
        fun onDeleteClicked(position: Int)

    }
}
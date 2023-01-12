package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ItemPrevOrdersBinding
import com.tawajood.vet.databinding.ItemPreviousConsultantBinding
import com.tawajood.vet.pojo.Consultant
import com.tawajood.vet.pojo.Order

class PreviousOrdersAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<PreviousOrdersAdapter.PreviousOrdersViewHolder>() {

    var orders = mutableListOf<Order>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PreviousOrdersViewHolder(val binding: ItemPrevOrdersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviousOrdersAdapter.PreviousOrdersViewHolder {
        val binding =
            ItemPrevOrdersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PreviousOrdersAdapter.PreviousOrdersViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PreviousOrdersAdapter.PreviousOrdersViewHolder,
        position: Int
    ) {
        holder.binding.date.text = orders[position].created_at.substring(0, 10)
        holder.binding.status.text = orders[position].statusName
        holder.binding.name.text = orders[position].carts[0].product_name
        holder.binding.quantity.text = orders[position].carts[0].quantity.toString()
        if (orders[position].status==0){
            holder.binding.status.setTextColor(holder.itemView.context.getColor(R.color.light_blue))
        }
        if (orders[position].status==1){
            holder.binding.status.setTextColor(holder.itemView.context.getColor(R.color.light_green))
        }
        if (orders[position].status==2){
            holder.binding.status.setTextColor(holder.itemView.context.getColor(R.color.red_400))
        }
        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
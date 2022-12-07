package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemCatBinding
import com.tawajood.vet.databinding.ItemConsultantBinding
import com.tawajood.vet.pojo.Category
import com.tawajood.vet.pojo.Consultant

class CategoriesAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    var categories = mutableListOf<Category>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CategoriesViewHolder(val binding: ItemCatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoriesViewHolder {
        val binding =
            ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesAdapter.CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoriesAdapter.CategoriesViewHolder,
        position: Int
    ) {
        Glide.with(holder.itemView.context).load(categories[position].image)
            .into(holder.binding.img)
        holder.binding.name.text = categories[position].name

        holder.itemView.setOnClickListener {
            onItemClick.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    interface OnItemClick {
        fun onItemClickListener(position: Int)
    }
}
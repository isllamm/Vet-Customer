package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemCommentBinding
import com.tawajood.vet.databinding.ItemProductBinding
import com.tawajood.vet.pojo.Product
import com.tawajood.vet.pojo.Recommendation

class ReviewAdapter() :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    var recommendations = mutableListOf<Recommendation>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class ReviewViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewViewHolder {
        val binding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewAdapter.ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        holder.binding.name.text = recommendations[position].user.name
        holder.binding.commentt.text = recommendations[position].comment

        holder.binding.tvRate.text = recommendations[position].rate.toString()
        Glide.with(holder.itemView.context).load(recommendations[position].user.image)
            .into(holder.binding.img)


    }

    override fun getItemCount(): Int {
        return recommendations.size
    }


}
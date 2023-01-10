package com.tawajood.vet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.ItemMessageBinding
import com.tawajood.vet.R
import com.tawajood.vet.pojo.Message

class MessagesAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(val binding: ItemMessageBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root)

    var messages = mutableListOf<Message>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()

        }

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        if (messages[position].sender == 1) {
            holder.binding.message.background =
                ContextCompat.getDrawable(
                    holder.context,
                    R.drawable.message_bg
                )
            holder.binding.ll.gravity = Gravity.START
        } else if (messages[position].sender == 0) {
            holder.binding.message.background =
                ContextCompat.getDrawable(
                    holder.context,
                    R.drawable.message_bg_2
                )
            holder.binding.ll.gravity = Gravity.END
        }

        if (messages[position].message_type == 2) {
            holder.binding.imageCard.isVisible = true
            holder.binding.message.isVisible = false
            Glide.with(holder.itemView)
                .load(messages[position].message)
                .into(holder.binding.image)
        }
        if (messages[position].message_type == 0) {
            holder.binding.imageCard.isVisible = false
            holder.binding.message.isVisible = true
            holder.binding.message.text = messages[position].message
        }

        holder.binding.image.setOnClickListener {
            onItemClick.onImageClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    interface OnItemClick {
        fun onImageClickListener(position: Int)
    }
}

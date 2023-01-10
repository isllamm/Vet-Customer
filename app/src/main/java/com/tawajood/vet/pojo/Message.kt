package com.tawajood.vet.pojo

data class Message(
    val id: Int,
    val chat_id: Int,
    val clinic_id: Int,
    val user_id: Int,
    val message: String,
    val message_type: Int,
    val sender: Int,
    val time: String,
)
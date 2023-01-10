package com.tawajood.vet.pojo

data class Chat(
    val id: Int,
    val user_name:String,
    val user_id:Int,
    val user_image:String,
    val request_id:Int,
    val messages:MutableList<Message>
)
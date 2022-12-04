package com.tawajood.vet.pojo

data class NotificationData(
    val id: Int,
    val date: String,
    val time: String,
    val body: String,
    val user_name: String,
    val request_id: Int,
    val status_request: Int,
)
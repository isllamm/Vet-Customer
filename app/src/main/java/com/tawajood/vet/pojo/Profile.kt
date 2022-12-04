package com.tawajood.vet.pojo

data class Profile (
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val image: String,
    val country_code: String,
    val lat: String,
    val lng: String,
    val address: String,
    val fcm_token: String,
    val mobile_id: String,
    val active: Int,
    val notifiable: Int,
)
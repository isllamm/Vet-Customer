package com.tawajood.vet.pojo

data class AddRequestBody(
    val user_id: Int,
    val clinic_id: Int,
    val pet_id: Int,
    val specialization_id: Int,
    val type_id: Int,
    val details: String,
    val clinic_day_id: Int,
    val clinic_time_id: Int,
    val address: String,
    val lat: Double,
    val lng: Double,
    val date: String,
)
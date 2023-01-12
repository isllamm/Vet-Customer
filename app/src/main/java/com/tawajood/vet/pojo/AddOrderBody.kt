package com.tawajood.vet.pojo

import retrofit2.http.Field

data class AddOrderBody(
    val user_id: String,
    val user_phone: String,
    val country_code: String,
    val address: String,
    val lat: String,
    val lng: String,
    val name: String,
    val payment_method: String,
)
package com.tawajood.vet.pojo

data class Order(
    val id: Int,
    val user_id: Int,
    val user_phone: String,
    val country_code: String,
    val status: Int,
    val statusName: String,
    val total: String,
    val tax: Int,
    val name: String,
    val payment_method: Int,
    val delivery_cost: Int,
    val discount: Int,
    val final_total: String,
    val lat: String,
    val lng: String,
    val address: String,
    val carts: MutableList<Cart>,
    val created_at:String,
)
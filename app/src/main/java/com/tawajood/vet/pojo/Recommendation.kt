package com.tawajood.vet.pojo

data class Recommendation(
    val id: Int,
    val rate: Int,
    val comment: String,
    val user_id: Int,
    val user: User
)
package com.tawajood.vet.pojo

data class Pet(
    val id: Int?,
    val user_id: Int?,
    val name: String?,
    val age: Int?,
    val weight: Int?,
    val gender: Int?,
    val image: String?,
    val type_id: Int?,
    val type: String?,
    val created_at: String?,
    val requests: MutableList<Consultant>?,
    val vaccinations: MutableList<Vaccinations>?,
    val birth_date: String?,
    val sterile: Int?,
    val breed: String?,
    var isSelected: Boolean = false
)
package com.tawajood.vet.pojo

data class Vaccinations(
    val id: Int,
    val pet_id: Int,
    val date: String,
    val type_id: Int,
    val vaccinationType: String
)
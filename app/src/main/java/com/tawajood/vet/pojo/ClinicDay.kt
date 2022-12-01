package com.tawajood.vet.pojo

data class ClinicDay(
    val id: Int,
    val clinic_id: Int,
    val day_id: Int,
    val day:Day,
    val times:MutableList<Times>,
)
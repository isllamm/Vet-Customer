package com.tawajood.vet.pojo

data class Clinic(
    val id: Int,
    val image: String,
    val name: String,
    val rate: Float,
    val count_clinic_rate: Int,
    val consultation_fees: Int,
    val consultation_duration: String,
    val details: String,
    val image_clinic:String,
    val phone: String,
    val country_code: String,
    val email: String,
    val registration_number: String,
    val address: String,
    val lat: String,
    val lng: String,
    val specializations: MutableList<Specialization>,
    val clinic_days: MutableList<ClinicDay>,
    val status_online: Int,
    val fcm_token: String,
    val mobile_id: String,
    val active: Int,
    val notifiable: Int,
    val locale: String,
    val recommendations:MutableList<Recommendation>
)
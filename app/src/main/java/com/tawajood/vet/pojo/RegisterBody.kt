package com.tawajood.vet.pojo

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    var name: String,
    @SerializedName("country_code")
    var countryCode: String,
    var phone: String,
    var email: String,
    var password: String,
)
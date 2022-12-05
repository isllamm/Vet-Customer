package com.tawajood.vet.pojo

import com.google.gson.annotations.SerializedName

data class ConsultantResponse(
    @SerializedName("requests")
    val consultant: MutableList<Consultant>,
)
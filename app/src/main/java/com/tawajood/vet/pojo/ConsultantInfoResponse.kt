package com.tawajood.vet.pojo

import com.google.gson.annotations.SerializedName

data class ConsultantInfoResponse(
    @SerializedName("request")
    val consultant: Consultant,
)
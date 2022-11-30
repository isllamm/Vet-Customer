package com.tawajood.vet.pojo

import com.google.gson.annotations.SerializedName


data class Exist(
    @SerializedName("0")
    var message: String,
    var forgetcode:Int,
)
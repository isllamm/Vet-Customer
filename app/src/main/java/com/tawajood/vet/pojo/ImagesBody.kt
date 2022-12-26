package com.tawajood.vet.pojo

import com.google.gson.annotations.SerializedName
import java.io.File

data class ImagesBody(
    @SerializedName("images[]")
    var images: List<File>? = null
)
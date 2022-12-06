package com.tawajood.vet.utils

import com.tawajood.vet.pojo.PetBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart


fun PetBody.toMap(): Map<String, RequestBody> {
    val addPet = hashMapOf<String, RequestBody>()
    addPet["user_id"] =
        user_id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["name"] =
        name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["age"] =
        age.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["weight"] =
        weight.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["gender"] =
        gender.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["type_id"] =
        type_id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["birth_date"] =
        birth_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["sterile"] =
        sterile.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    addPet["breed"] =
        breed.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    return addPet
}
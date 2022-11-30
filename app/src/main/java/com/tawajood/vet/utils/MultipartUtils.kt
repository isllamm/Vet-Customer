package com.tawajood.vetclinic.utils

import com.tawajood.vetclinic.pojo.UpdatedBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart


fun UpdatedBody.toMap(): Map<String, RequestBody> {

    val updateMap = hashMapOf<String, RequestBody>()


    updateMap["name"] = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["phone"] = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["country_code"] = countryCode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["email"] = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["registration_number"] =
        registration_number.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["consultation_fees"] =
        consultation_fees.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["consultation_duration"] =
        consultation_duration.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["address"] = address.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["details"] = details.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["status_online"] =
        status_online.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["lat"] = lat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    updateMap["lng"] = lng.toRequestBody("multipart/form-data".toMediaTypeOrNull())


    return updateMap
}

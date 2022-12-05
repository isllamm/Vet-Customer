package com.tawajood.vet.repository

import PrefsHelper
import com.tawajood.vet.api.RetrofitApi
import com.tawajood.vet.pojo.RegisterBody
import javax.inject.Inject

class Repository
@Inject
constructor(private val api: RetrofitApi) {


    suspend fun register(registerBody: RegisterBody) =
        api.register(PrefsHelper.getLanguage(), registerBody)

    suspend fun login(countryCode: String, phone: String, password: String) =
        api.login(PrefsHelper.getLanguage(), countryCode, phone, password)

    suspend fun checkPhone(countryCode: String, phone: String, security: String) =
        api.checkPhone(PrefsHelper.getLanguage(), countryCode, phone, security)

    suspend fun forgetPassword(
        countryCode: String,
        phone: String,
        password: String,
        forgetcode: String,
        security: String
    ) =
        api.forgetPassword(
            PrefsHelper.getLanguage(),
            countryCode,
            phone,
            password,
            forgetcode,
            security
        )

    suspend fun getProfile() =
        api.getProfile(PrefsHelper.getLanguage(), PrefsHelper.getToken())

    suspend fun terms() = api.terms(PrefsHelper.getLanguage())
    suspend fun contact() = api.contactUs(PrefsHelper.getLanguage())
    suspend fun getNotifications() =
        api.getNotifications(PrefsHelper.getLanguage(), PrefsHelper.getUserId().toString())

    suspend fun getSpecialties() =
        api.getSpecialties(PrefsHelper.getLanguage())

    suspend fun getOnlineClinics() =
        api.getOnlineClinics(PrefsHelper.getLanguage())

    suspend fun getMostRatedClinics() =
        api.getMostRatedClinics(PrefsHelper.getLanguage())

    suspend fun getClinicsBySpecialization(id: String) =
        api.getClinicsBySpecialization(PrefsHelper.getLanguage(), id)

    suspend fun searchClinics(name: String) =
        api.searchClinics(PrefsHelper.getLanguage(), name)

    suspend fun getMyRequests() =
        api.getMyRequests(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString()
        )

    suspend fun getRequestById(requestId: String) =
        api.getRequestById(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            requestId,
        )
}
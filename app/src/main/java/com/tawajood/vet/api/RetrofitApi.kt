package com.tawajood.vet.api

import com.tawajood.vet.pojo.*
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    companion object {
        const val BASE_URL = "https://vet.horizon.net.sa/api/"
    }

    @POST("register")
    suspend fun register(
        @Header("lang") lang: String,
        @Body registerBody: RegisterBody
    ): Response<MainResponse<UserResponse>>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Header("lang") lang: String,
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<MainResponse<UserResponse>>


    @FormUrlEncoded
    @POST("check-phone")
    suspend fun checkPhone(
        @Header("lang") lang: String,
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("security") security: String,
    ): Response<MainResponse<Exist>>

    @FormUrlEncoded
    @POST("forget-password")
    suspend fun forgetPassword(
        @Header("lang") lang: String,
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("forgetcode") forgetcode: String,
        @Field("security") security: String,
    ): Response<MainResponse<Any>>


    @GET("get-specialties")
    suspend fun getSpecialties(
        @Header("lang") lang: String,
    ): Response<MainResponse<SpecialtiesResponse>>

    @GET("get-online-clinics")
    suspend fun getOnlineClinics(
        @Header("lang") lang: String,
    ): Response<MainResponse<ClinicsResponse>>

    @GET("most-rated-clinics")
    suspend fun getMostRatedClinics(
        @Header("lang") lang: String,
    ): Response<MainResponse<ClinicsResponse>>

    @FormUrlEncoded
    @POST("get-clinics-by-specialization")
    suspend fun getClinicsBySpecialization(
        @Header("lang") lang: String,
        @Field("id") id: String,
    ): Response<MainResponse<ClinicsResponse>>

    @FormUrlEncoded
    @POST("search-clinics")
    suspend fun searchClinics(
        @Header("lang") lang: String,
        @Field("name") name: String,
    ): Response<MainResponse<ClinicsResponse>>
}
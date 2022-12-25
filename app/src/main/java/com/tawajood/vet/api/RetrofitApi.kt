package com.tawajood.vet.api

import com.tawajood.vet.pojo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("profile")
    suspend fun getProfile(
        @Header("lang") lang: String,
        @Header("token") token: String
    ): Response<MainResponse<ProfileResponse>>

    @GET("setting/terms")
    suspend fun terms(
        @Header("lang") lang: String,
    ): Response<MainResponse<Terms>>

    @GET("setting/contact-us")
    suspend fun contactUs(
        @Header("lang") lang: String,
    ): Response<MainResponse<ContactUsResponse>>

    @FormUrlEncoded
    @GET("notifications")
    suspend fun getNotifications(
        @Header("lang") lang: String,
        @Field("user_id") user_id: String,
    ): Response<MainResponse<NotificationResponse>>

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

    @FormUrlEncoded
    @POST("my-requests")
    suspend fun getMyRequests(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
    ): Response<MainResponse<ConsultantResponse>>

    @FormUrlEncoded
    @POST("request-by-id")
    suspend fun getRequestById(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("request_id") request_id: String,
    ): Response<MainResponse<ConsultantInfoResponse>>

    @FormUrlEncoded
    @POST("my-pets")
    suspend fun getMyPets(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
    ): Response<MainResponse<PetsResponse>>

    @Multipart
    @JvmSuppressWildcards
    @POST("add-pet")
    suspend fun addPet(
        @Header("token") token: String,
        @Header("lang") lang: String,
        @PartMap petBody: Map<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<MainResponse<Any>>

    @POST("add-pet")
    suspend fun addPet(
        @Header("token") token: String,
        @Header("lang") lang: String,
        @Body addPet: PetBody,
    ): Response<MainResponse<Any>>

    @GET("pet-types")
    suspend fun getPetTypes(
        @Header("lang") lang: String,
    ): Response<MainResponse<PetsTypeResponse>>

    @GET("vaccination-types")
    suspend fun getVaccinationTypes(
        @Header("lang") lang: String,
    ): Response<MainResponse<VaccinationTypeResponse>>

    @GET("categories")
    suspend fun getCategories(
        @Header("lang") lang: String,
    ): Response<MainResponse<CategoriesResponse>>

    @FormUrlEncoded
    @POST("pet-by-id")
    suspend fun getPetById(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("pet_id") pet_id: String
    ): Response<MainResponse<PetResponse>>

    @FormUrlEncoded
    @POST("add-vaccination")
    suspend fun addVaccination(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("pet_id") pet_id: String,
        @Field("date") date: String,
        @Field("type") type: String
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("get-clinic-by-id")
    suspend fun getClinicById(
        @Header("lang") lang: String,
        @Field("id") clinicId: String,
    ): Response<MainResponse<ClinicsResponse>>

    @GET("request-types")
    suspend fun getRequestTypes(
        @Header("lang") lang: String,
    ): Response<MainResponse<RequestTypesResponse>>
}
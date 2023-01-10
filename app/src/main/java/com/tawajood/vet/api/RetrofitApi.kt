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
    @POST("pay-fees")
    suspend fun payFees(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("request_id") request_id: String,
        @Field("user_id") user_id: String,
        @Field("clinic_id") clinic_id: String,
    ): Response<MainResponse<Any>>

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

    @GET("get-vendors-by-category-id")
    suspend fun getVendors(
        @Header("lang") lang: String,
        @Query("cat_id") cat_id: String
    ): Response<MainResponse<VendorsResponse>>

    @GET("get-subcategories-by-vendor-id")
    suspend fun getSubcategories(
        @Header("lang") lang: String,
        @Query("vendor_id") vendor_id: String
    ): Response<MainResponse<SubcategoriesResponse>>


    @GET("get-products-by-subcategory-id")
    suspend fun getProducts(
        @Header("lang") lang: String,
        @Query("subcategory_id") subcategory_id: String
    ): Response<MainResponse<ProductsResponse>>

    @GET("get-product-by-id")
    suspend fun getProductById(
        @Header("lang") lang: String,
        @Query("product_id") product_id: String
    ): Response<MainResponse<ProductResponse>>

    @FormUrlEncoded
    @POST("add-cart")
    suspend fun addToCart(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
        @Field("product_id") product_id: String,
        @Field("quantity") quantity: String,
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("cart")
    suspend fun getCart(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
    ): Response<MainResponse<CartResponse>>

    @FormUrlEncoded
    @POST("update-cart")
    suspend fun deleteItemCart(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("cart_id") cart_id: String,
        @Field("quantity") quantity: String,
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("my-orders")
    suspend fun getMyOrders(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("add-order")
    suspend fun addOrder(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
        @Field("user_phone") user_phone: String,
        @Field("country_code") country_code: String,
        @Field("address") address: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("name") name: String,
        @Field("payment_method") payment_method: String,
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("rate-clinic")
    suspend fun reviewDoctor(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
        @Field("clinic_id") clinic_id: String,
        @Field("rate") rate: String,
        @Field("comment") comment: String,
    ): Response<MainResponse<Any>>

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

    @FormUrlEncoded
    @POST("get-times-unreserved")
    suspend fun getTimesUnreserved(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("clinic_id") clinic_id: String,
        @Field("clinic_day_id") clinic_day_id: String,
        @Field("date") date: String,
    ): Response<MainResponse<TimesResponse>>

    @Multipart
    @JvmSuppressWildcards
    @POST("add-request")
    suspend fun addRequest(
        @Header("token") token: String,
        @Header("lang") lang: String,
        @PartMap requestBody: Map<String, RequestBody>,
        @Part images: Array<MultipartBody.Part>
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("chats/send-message")
    suspend fun sendMessage(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("request_id") request_id: String,
        @Field("user_id") user_id: String,
        @Field("message") message: String,
        @Field("message_type") message_type: String
    ): Response<MainResponse<Any>>

    @FormUrlEncoded
    @POST("chats/get-chat")
    suspend fun getChat(
        @Header("lang") lang: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
        @Field("request_id") request_id: String
    ): Response<MainResponse<ChatResponse>>
}
package com.tawajood.vet.api

import com.tawajood.vet.pojo.Exist
import com.tawajood.vet.pojo.MainResponse
import com.tawajood.vet.pojo.RegisterBody
import com.tawajood.vet.pojo.Token
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    companion object {
        const val BASE_URL = "https://vet.horizon.net.sa/"
    }

    @POST("register")
    suspend fun register(
        @Header("lang") lang: String,
        @Body registerBody: RegisterBody
    ): Response<MainResponse<Token>>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Header("lang") lang: String,
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<MainResponse<Token>>


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
}
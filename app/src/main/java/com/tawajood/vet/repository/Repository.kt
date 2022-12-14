package com.tawajood.vet.repository

import PrefsHelper
import com.tawajood.vet.api.RetrofitApi
import com.tawajood.vet.pojo.*
import com.tawajood.vet.utils.toMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
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

    suspend fun payFees(requestId: String, clinic_id: String) =
        api.payFees(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            requestId,
            PrefsHelper.getUserId().toString(),
            clinic_id,
        )

    suspend fun cancelRequest(requestId: String) =
        api.cancelRequest(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            requestId,
        )

    suspend fun getMyPets() =
        api.getMyPets(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString()
        )

    suspend fun addPet(petBody: PetBody): Response<MainResponse<Any>> {
        if (petBody.image != null) {
            val imagePart = MultipartBody.Part.createFormData(
                "image",
                petBody.image.name,
                petBody.image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
            return api.addPet(
                PrefsHelper.getToken(),
                PrefsHelper.getLanguage(),
                petBody.toMap(),
                imagePart
            )
        } else {
            return api.addPet(
                PrefsHelper.getToken(),
                PrefsHelper.getLanguage(),
                petBody,
            )
        }
    }

    suspend fun getPetTypes() =
        api.getPetTypes(PrefsHelper.getLanguage())

    suspend fun getVaccinationTypes() =
        api.getVaccinationTypes(PrefsHelper.getLanguage())

    suspend fun getCategories() =
        api.getCategories(PrefsHelper.getLanguage())

    suspend fun getVendors(id: String) =
        api.getVendors(PrefsHelper.getLanguage(), id)

    suspend fun getSubcategories(id: String) =
        api.getSubcategories(PrefsHelper.getLanguage(), id)

    suspend fun getProducts(id: String) =
        api.getProducts(PrefsHelper.getLanguage(), id)

    suspend fun getProductById(id: String) =
        api.getProductById(PrefsHelper.getLanguage(), id)

    suspend fun addToCart(product_id: String, quantity: String) =
        api.addToCart(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString(),
            product_id,
            quantity,
        )

    suspend fun getCart() =
        api.getCart(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString(),
        )

    suspend fun deleteItemCart(cartId: String) =
        api.deleteItemCart(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            cartId,
            "1"
        )

    suspend fun getMyOrders() =
        api.getMyOrders(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString(),
        )

    suspend fun addOrder(
        addOrderBody: AddOrderBody
    ) =
        api.addOrder(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            addOrderBody
        )

    suspend fun reviewDoctor(clinic_id: String, rate: String, comment: String) =
        api.reviewDoctor(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            PrefsHelper.getUserId().toString(),
            clinic_id,
            rate,
            comment
        )

    suspend fun getPetById(petId: String) =
        api.getPetById(PrefsHelper.getLanguage(), PrefsHelper.getToken(), petId)

    suspend fun addVaccination(petId: String, date: String, type: String) =
        api.addVaccination(PrefsHelper.getLanguage(), PrefsHelper.getToken(), petId, date, type)

    suspend fun getClinicById(id: String) =
        api.getClinicById(PrefsHelper.getLanguage(), id)

    suspend fun getRequestTypes() =
        api.getRequestTypes(PrefsHelper.getLanguage())

    suspend fun getTimesUnreserved(clinic_id: String, clinic_day_id: String, date: String) =
        api.getTimesUnreserved(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            clinic_id,
            clinic_day_id,
            date
        )


    suspend fun addRequest(
        addRequestBody: AddRequestBody, images: ImagesBody
    ): Response<MainResponse<Any>> {

        val imagesParts: MutableList<MultipartBody.Part> = mutableListOf()
        images.images!!.forEach {
            imagesParts.add(
                MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            )
        }

        return api.addRequest(
            PrefsHelper.getToken(),
            PrefsHelper.getLanguage(),
            addRequestBody.toMap(),
            imagesParts.toTypedArray()
        )
    }

    suspend fun getChat(request_id: String, user_id: String) =
        api.getChat(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            user_id,
            request_id
        )

    suspend fun sendMessage(
        request_id: String,
        user_id: String,
        message: String,
        message_type: String
    ) =
        api.sendMessage(
            PrefsHelper.getLanguage(),
            PrefsHelper.getToken(),
            request_id,
            user_id,
            message,
            message_type
        )
}
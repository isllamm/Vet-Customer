package com.tawajood.vet.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.Exist
import com.tawajood.vet.pojo.RegisterBody
import com.tawajood.vet.pojo.Token
import com.tawajood.vet.repository.Repository
import com.tawajood.vetclinic.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _userLoginFlow = MutableSharedFlow<Resource<Token>>()
    val userLoginFlow = _userLoginFlow.asSharedFlow()

    private val _userRegisterFlow = MutableSharedFlow<Resource<Token>>()
    val userRegisterFlow = _userRegisterFlow.asSharedFlow()

    private val _checkPhone = MutableStateFlow<Resource<Exist>>(Resource.Idle())
    val checkPhone = _checkPhone.asSharedFlow()

    private val _forgetPassword = MutableStateFlow<Resource<Any>>(Resource.Idle())
    val forgetPassword = _forgetPassword.asSharedFlow()


    fun login(countryCode: String, phone: String, password: String) = viewModelScope.launch {
        try {
            _userLoginFlow.emit(Resource.Loading())
            val response = handleResponse(repository.login(countryCode, phone, password))
            if (response.status) {
                _userLoginFlow.emit(Resource.Success(response.data!!))
            } else {
                _userLoginFlow.emit(Resource.Error(message = response.msg))
                Log.d("islam", response.msg)

            }
        } catch (e: Exception) {
            Log.d("islam", e.message.toString())
            _userLoginFlow.emit(Resource.Error(message = e.message.toString()))
        }
    }


    fun register(registerBody: RegisterBody) = viewModelScope.launch {
        try {
            _userRegisterFlow.emit(Resource.Loading())
            val response = handleResponse(repository.register(registerBody))
            if (response.status) {
                _userRegisterFlow.emit(Resource.Success(response.data!!))
            } else {
                _userRegisterFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _userRegisterFlow.emit(Resource.Error(message = e.message.toString()))
        }
    }


    fun checkPhone(countryCode: String, phone: String) = viewModelScope.launch {
        try {
            _checkPhone.emit(Resource.Loading())
            val response = handleResponse(repository.checkPhone(countryCode, phone, "***"))
            if (response.status) {
                _checkPhone.emit(Resource.Success(response.data!!))
            } else {
                _checkPhone.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _checkPhone.emit(Resource.Error(message = e.message!!))
        }
    }

    fun forgetPassword(
        countryCode: String,
        phone: String,
        password: String,
        forgetcode: String,
    ) = viewModelScope.launch {
        try {
            _forgetPassword.emit(Resource.Loading())
            val response = handleResponse(
                repository.forgetPassword(
                    countryCode,
                    phone,
                    password,
                    forgetcode,
                    "***"
                )
            )
            if (response.status) {
                _forgetPassword.emit(Resource.Success(response.data!!))
            } else {
                _forgetPassword.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _forgetPassword.emit(Resource.Error(message = e.message!!))
        }
    }
}
package com.tawajood.vet.ui.main.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.ClinicsResponse
import com.tawajood.vet.pojo.RequestTypesResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getDoctorProfileFlow =
        MutableStateFlow<Resource<ClinicsResponse>>(Resource.Idle())
    val getDoctorProfileFlow = _getDoctorProfileFlow.asSharedFlow()

    private val _getRequestTypesFlow =
        MutableStateFlow<Resource<RequestTypesResponse>>(Resource.Idle())
    val getRequestTypesFlow = _getRequestTypesFlow.asSharedFlow()


    fun getDoctorProfile(id: String) = viewModelScope.launch {
        try {
            _getDoctorProfileFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getClinicById(id))
            if (response.status) {
                _getDoctorProfileFlow.emit(Resource.Success(response.data!!))
            } else {
                _getDoctorProfileFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getDoctorProfileFlow.emit(Resource.Error(message = e.message!!))
        }
    }


    fun getRequestTypes() = viewModelScope.launch {
        try {
            _getRequestTypesFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getRequestTypes())
            if (response.status) {
                _getRequestTypesFlow.emit(Resource.Success(response.data!!))
            } else {
                _getRequestTypesFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getRequestTypesFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
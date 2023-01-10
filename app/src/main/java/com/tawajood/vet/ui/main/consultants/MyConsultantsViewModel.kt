package com.tawajood.vet.ui.main.consultants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.ConsultantInfoResponse
import com.tawajood.vet.pojo.ConsultantResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConsultantsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getMyRequestsFlow =
        MutableStateFlow<Resource<ConsultantResponse>>(Resource.Idle())
    val getMyRequestsFlow = _getMyRequestsFlow.asSharedFlow()

    private val _getRequestByIdFlow =
        MutableStateFlow<Resource<ConsultantInfoResponse>>(Resource.Idle())
    val getRequestByIdFlow = _getRequestByIdFlow.asSharedFlow()

    private val _payFeesFlow =
        MutableStateFlow<Resource<Any>>(Resource.Idle())
    val payFeesFlow = _payFeesFlow.asSharedFlow()

    init {
        getMyRequests()
    }

    private fun getMyRequests() = viewModelScope.launch {
        try {
            _getMyRequestsFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getMyRequests())
            if (response.status) {
                _getMyRequestsFlow.emit(Resource.Success(response.data!!))
            } else {
                _getMyRequestsFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getMyRequestsFlow.emit(Resource.Error(message = e.message!!))
        }
    }


    fun getRequestById(requestId: String) = viewModelScope.launch {
        try {
            _getRequestByIdFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getRequestById(requestId))
            if (response.status) {
                _getRequestByIdFlow.emit(Resource.Success(response.data!!))
            } else {
                _getRequestByIdFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getRequestByIdFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun payFees(requestId: String, clinic_id: String) = viewModelScope.launch {
        try {
            _payFeesFlow.emit(Resource.Loading())
            val response = handleResponse(repository.payFees(requestId, clinic_id))
            if (response.status) {
                _payFeesFlow.emit(Resource.Success(response.data!!))
            } else {
                _payFeesFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _payFeesFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
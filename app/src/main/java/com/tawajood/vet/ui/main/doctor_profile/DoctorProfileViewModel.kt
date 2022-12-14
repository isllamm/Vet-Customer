package com.tawajood.vet.ui.main.doctor_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.ClinicsResponse
import com.tawajood.vet.pojo.ProfileResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorProfileViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getDoctorProfileFlow =
        MutableStateFlow<Resource<ClinicsResponse>>(Resource.Idle())
    val getDoctorProfileFlow = _getDoctorProfileFlow.asSharedFlow()


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
}
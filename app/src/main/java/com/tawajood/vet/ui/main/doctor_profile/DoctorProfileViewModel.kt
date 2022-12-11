package com.tawajood.vet.ui.main.doctor_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _getProfileFlow =
        MutableStateFlow<Resource<ProfileResponse>>(Resource.Idle())
    val getProfileFlow = _getProfileFlow.asSharedFlow()


    private fun getProfile() = viewModelScope.launch {
        try {
            _getProfileFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getProfile())
            if (response.status) {
                _getProfileFlow.emit(Resource.Success(response.data!!))
            } else {
                _getProfileFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getProfileFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
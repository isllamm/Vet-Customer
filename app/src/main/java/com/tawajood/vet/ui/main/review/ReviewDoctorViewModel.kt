package com.tawajood.vet.ui.main.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.ClinicsResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewDoctorViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _reviewFlow =
        MutableStateFlow<Resource<Any>>(Resource.Idle())
    val reviewFlow = _reviewFlow.asSharedFlow()

    fun reviewDoctor(clinic_id: String, rate: String, comment: String) = viewModelScope.launch {
        try {
            _reviewFlow.emit(Resource.Loading())
            val response = handleResponse(
                repository.reviewDoctor(
                    clinic_id,
                    rate,
                    comment
                )
            )
            if (response.status) {
                _reviewFlow.emit(Resource.Success(response.data!!))
            } else {
                _reviewFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _reviewFlow.emit(Resource.Error(message = e.message!!))
        }
    }


}
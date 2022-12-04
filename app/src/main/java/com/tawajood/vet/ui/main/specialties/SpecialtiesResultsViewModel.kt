package com.tawajood.vet.ui.main.specialties

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
class SpecialtiesResultsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getSpecialtiesResultFlow =
        MutableStateFlow<Resource<ClinicsResponse>>(Resource.Idle())
    val getSpecialtiesResultFlow = _getSpecialtiesResultFlow.asSharedFlow()


    fun getClinicsBySpecialization(specialtiesID: String) = viewModelScope.launch {
        try {
            _getSpecialtiesResultFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getClinicsBySpecialization(specialtiesID))
            if (response.status) {
                _getSpecialtiesResultFlow.emit(Resource.Success(response.data!!))
            } else {
                _getSpecialtiesResultFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getSpecialtiesResultFlow.emit(Resource.Error(message = e.message!!))
        }
    }

}
package com.tawajood.vet.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.SpecialtiesResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getSpecialtiesFlow =
        MutableStateFlow<Resource<SpecialtiesResponse>>(Resource.Idle())
    val getSpecialtiesFlow = _getSpecialtiesFlow.asSharedFlow()

    init {
        getSpecialties()
    }

    private fun getSpecialties() = viewModelScope.launch {
        try {
            _getSpecialtiesFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getSpecialties())
            if (response.status) {
                _getSpecialtiesFlow.emit(Resource.Success(response.data!!))
            } else {
                _getSpecialtiesFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getSpecialtiesFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
package com.tawajood.vet.ui.main.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.*
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _addPet = MutableStateFlow<Resource<Any>>(Resource.Idle())
    val addPet = _addPet.asSharedFlow()

    private val _myPets = MutableStateFlow<Resource<PetsResponse>>(Resource.Idle())
    val myPets = _myPets.asSharedFlow()

    private val _myPetInfo = MutableStateFlow<Resource<PetResponse>>(Resource.Idle())
    val myPetInfo = _myPetInfo.asSharedFlow()

    private val _getPetTypes = MutableStateFlow<Resource<PetsTypeResponse>>(Resource.Idle())
    val getPetTypes = _getPetTypes.asSharedFlow()

    private val _getVaccinationTypes =
        MutableStateFlow<Resource<VaccinationTypeResponse>>(Resource.Idle())
    val getVaccinationTypes = _getVaccinationTypes.asSharedFlow()

    private val _addVaccination =
        MutableStateFlow<Resource<Any>>(Resource.Idle())
    val addVaccination = _addVaccination.asSharedFlow()

    init {
        getMyPets()

    }

    fun addPet(petBody: PetBody) = viewModelScope.launch {
        try {
            _addPet.emit(Resource.Loading())
            val response = handleResponse(repository.addPet(petBody))
            if (response.status) {
                _addPet.emit(Resource.Success(response.data!!))
            } else {
                _addPet.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _addPet.emit(Resource.Error(message = e.message!!))
        }
    }


    private fun getMyPets() = viewModelScope.launch {
        try {
            _myPets.emit(Resource.Loading())
            val response = handleResponse(repository.getMyPets())
            if (response.status) {
                _myPets.emit(Resource.Success(response.data!!))
            } else {
                _myPets.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _myPets.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getPetById(petId: String) = viewModelScope.launch {
        try {
            _myPetInfo.emit(Resource.Loading())
            val response = handleResponse(repository.getPetById(petId))
            if (response.status) {
                _myPetInfo.emit(Resource.Success(response.data!!))
            } else {
                _myPetInfo.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _myPetInfo.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getPetTypes() = viewModelScope.launch {
        try {
            _getPetTypes.emit(Resource.Loading())
            val response = handleResponse(repository.getPetTypes())
            if (response.status) {
                _getPetTypes.emit(Resource.Success(response.data!!))
            } else {
                _getPetTypes.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getPetTypes.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getVaccinationTypes() = viewModelScope.launch {
        try {
            _getVaccinationTypes.emit(Resource.Loading())
            val response = handleResponse(repository.getVaccinationTypes())
            if (response.status) {
                _getVaccinationTypes.emit(Resource.Success(response.data!!))
            } else {
                _getVaccinationTypes.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getVaccinationTypes.emit(Resource.Error(message = e.message!!))
        }
    }

    fun addVaccination(petId: String, date: String, type: String) = viewModelScope.launch {
        try {
            _addVaccination.emit(Resource.Loading())
            val response = handleResponse(repository.addVaccination(petId, date, type))
            if (response.status) {
                _addVaccination.emit(Resource.Success(response.data!!))
            } else {
                _addVaccination.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _addVaccination.emit(Resource.Error(message = e.message!!))
        }
    }
}
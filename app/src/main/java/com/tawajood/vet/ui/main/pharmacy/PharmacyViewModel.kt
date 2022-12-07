package com.tawajood.vet.ui.main.pharmacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.CategoriesResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _getCategoriesFlow = MutableStateFlow<Resource<CategoriesResponse>>(Resource.Idle())
    val getCategoriesFlow = _getCategoriesFlow.asSharedFlow()

    init {
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        try {
            _getCategoriesFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getCategories())
            if (response.status) {
                _getCategoriesFlow.emit(Resource.Success(response.data!!))
            } else {
                _getCategoriesFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getCategoriesFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
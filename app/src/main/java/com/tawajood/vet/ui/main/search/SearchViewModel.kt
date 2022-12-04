package com.tawajood.vet.ui.main.search

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
class SearchViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getSearchResultFlow =
        MutableStateFlow<Resource<ClinicsResponse>>(Resource.Idle())
    val getSearchResultFlow = _getSearchResultFlow.asSharedFlow()


    fun searchClinics(name: String) = viewModelScope.launch {
        try {
            _getSearchResultFlow.emit(Resource.Loading())
            val response = handleResponse(repository.searchClinics(name))
            if (response.status) {
                _getSearchResultFlow.emit(Resource.Success(response.data!!))
            } else {
                _getSearchResultFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getSearchResultFlow.emit(Resource.Error(message = e.message!!))
        }
    }

}
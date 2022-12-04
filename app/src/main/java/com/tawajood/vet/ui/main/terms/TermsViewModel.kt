package com.tawajood.vet.ui.main.terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.Terms
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _terms = MutableStateFlow<Resource<Terms>>(Resource.Idle())
    val terms = _terms.asSharedFlow()

    init {
        getTerms()
    }

    private fun getTerms() = viewModelScope.launch {
        try {
            _terms.emit(Resource.Loading())
            val response = handleResponse(repository.terms())
            if (response.status) {
                _terms.emit(Resource.Success(response.data!!))
            } else {
                _terms.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _terms.emit(Resource.Error(message = e.message!!))
        }
    }
}
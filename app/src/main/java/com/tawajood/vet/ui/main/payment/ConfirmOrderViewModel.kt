package com.tawajood.vet.ui.main.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.AddOrderBody
import com.tawajood.vet.pojo.CartResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getCartFlow =
        MutableStateFlow<Resource<CartResponse>>(Resource.Idle())
    val getCartFlow = _getCartFlow.asSharedFlow()

    private val _addOrderFlow =
        MutableStateFlow<Resource<Any>>(Resource.Idle())
    val addOrderFlow = _addOrderFlow.asSharedFlow()

    init {
        getCart()
    }

    private fun getCart() = viewModelScope.launch {
        try {
            _getCartFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getCart())
            if (response.status) {
                _getCartFlow.emit(Resource.Success(response.data!!))
            } else {
                _getCartFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getCartFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun addOrder(
        addOrderBody: AddOrderBody
    ) = viewModelScope.launch {
        try {
            _addOrderFlow.emit(Resource.Loading())
            val response = handleResponse(
                repository.addOrder(
                    addOrderBody
                )
            )
            if (response.status) {
                _addOrderFlow.emit(Resource.Success(response.data!!))
            } else {
                _addOrderFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _addOrderFlow.emit(Resource.Error(message = e.message!!))
        }
    }

}
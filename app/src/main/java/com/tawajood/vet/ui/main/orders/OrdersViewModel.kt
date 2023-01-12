package com.tawajood.vet.ui.main.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.CartResponse
import com.tawajood.vet.pojo.PreviousOrdersResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getOrdersFlow =
        MutableStateFlow<Resource<PreviousOrdersResponse>>(Resource.Idle())
    val getOrdersFlow = _getOrdersFlow.asSharedFlow()


    init {
        getMyOrders()
    }

    private fun getMyOrders() = viewModelScope.launch {
        try {
            _getOrdersFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getMyOrders())
            if (response.status) {
                _getOrdersFlow.emit(Resource.Success(response.data!!))
            } else {

                _getOrdersFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getOrdersFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
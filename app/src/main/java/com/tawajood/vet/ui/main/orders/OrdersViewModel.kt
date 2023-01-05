package com.tawajood.vet.ui.main.orders

import androidx.lifecycle.ViewModel
import com.tawajood.vet.pojo.CartResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _getOrdersFlow =
        MutableStateFlow<Resource<CartResponse>>(Resource.Idle())
    val getOrdersFlow = _getOrdersFlow.asSharedFlow()




}
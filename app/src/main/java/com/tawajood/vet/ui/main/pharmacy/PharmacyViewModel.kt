package com.tawajood.vet.ui.main.pharmacy

import android.util.Log
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
class PharmacyViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _getCategoriesFlow = MutableStateFlow<Resource<CategoriesResponse>>(Resource.Idle())
    val getCategoriesFlow = _getCategoriesFlow.asSharedFlow()

    private val _getVendorsFlow = MutableStateFlow<Resource<VendorsResponse>>(Resource.Idle())
    val getVendorsFlow = _getVendorsFlow.asSharedFlow()

    private val _getSubcategoriesFlow =
        MutableStateFlow<Resource<SubcategoriesResponse>>(Resource.Idle())
    val getSubcategoriesFlow = _getSubcategoriesFlow.asSharedFlow()

    private val _getProductsFlow = MutableStateFlow<Resource<ProductsResponse>>(Resource.Idle())
    val getProductsFlow = _getProductsFlow.asSharedFlow()

    private val _addToCartFlow = MutableStateFlow<Resource<Any>>(Resource.Idle())
    val addToCartFlow = _addToCartFlow.asSharedFlow()

    private val _getProductFlow = MutableStateFlow<Resource<ProductResponse>>(Resource.Idle())
    val getProductFlow = _getProductFlow.asSharedFlow()


    fun getCategories() = viewModelScope.launch {
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

    fun getVendors(catId: String) = viewModelScope.launch {
        try {
            _getVendorsFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getVendors(catId))
            if (response.status) {
                _getVendorsFlow.emit(Resource.Success(response.data!!))
            } else {
                Log.d("islam", "getVendors: ${response.msg}")

                _getVendorsFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            Log.d("islam", "getVendors: ${e.message}")
            _getVendorsFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getSubcategories(vendorId: String) = viewModelScope.launch {
        try {
            _getSubcategoriesFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getSubcategories(vendorId))
            if (response.status) {
                _getSubcategoriesFlow.emit(Resource.Success(response.data!!))
            } else {
                Log.d("islam", "getVendors: ${response.msg}")

                _getSubcategoriesFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            Log.d("islam", "getVendors: ${e.message}")
            _getSubcategoriesFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getProducts(subCatId: String) = viewModelScope.launch {
        try {
            _getProductsFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getProducts(subCatId))
            if (response.status) {
                _getProductsFlow.emit(Resource.Success(response.data!!))
            } else {
                Log.d("islam", "getVendors: ${response.msg}")

                _getProductsFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            Log.d("islam", "getVendors: ${e.message}")
            _getProductsFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun getProductById(productId: String) = viewModelScope.launch {
        try {
            _getProductFlow.emit(Resource.Loading())
            val response = handleResponse(repository.getProductById(productId))
            if (response.status) {
                _getProductFlow.emit(Resource.Success(response.data!!))
            } else {
                Log.d("islam", "getVendors: ${response.msg}")

                _getProductFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            Log.d("islam", "getVendors: ${e.message}")
            _getProductFlow.emit(Resource.Error(message = e.message!!))
        }
    }

    fun addToCart(product_id: String, quantity: String) = viewModelScope.launch {
        try {
            _addToCartFlow.emit(Resource.Loading())
            val response = handleResponse(repository.addToCart(product_id, quantity))
            if (response.status) {
                _addToCartFlow.emit(Resource.Success(response.data!!))
            } else {
                Log.d("islam", "getVendors: ${response.msg}")

                _addToCartFlow.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            Log.d("islam", "getVendors: ${e.message}")
            _addToCartFlow.emit(Resource.Error(message = e.message!!))
        }
    }
}
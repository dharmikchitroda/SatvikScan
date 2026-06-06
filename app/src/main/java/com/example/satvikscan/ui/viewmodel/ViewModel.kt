package com.example.satvikscan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satvikscan.Api_Calling.RetrofitInstance
import com.example.satvikscan.data.model.ProductResponse
import com.example.satvikscan.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Satvik_ViewModel : ViewModel() {

    // for this portion permission get or not
    private val _CameraPermission = MutableStateFlow(false)
    val CameraPermission: StateFlow<Boolean> = _CameraPermission


    // stop / start scanner
    private var _isScanning = MutableStateFlow(true)
    val isScanning: StateFlow<Boolean> = _isScanning

    // take response reposetry use and inject object
    private val repository = ProductRepository(RetrofitInstance.api)

    // check current status of api response
    private var _productUiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val productUiState: StateFlow<ProductUiState> = _productUiState

    sealed interface ProductUiState {

        data object Idle : ProductUiState
        data object Loading : ProductUiState
        data class Success(val product: ProductResponse) :
            ProductUiState // api respons data as actual object provoded by retrofit

        data class Error(val message: String) : ProductUiState
    }

    fun PermissionResult(isGranted: Boolean) {
        _CameraPermission.value = isGranted
    }

    fun stopscanning() {
        _isScanning.value = false
    }

    fun startScanning() {
        _isScanning.value = true
    }

    fun ChangeproductUiState(barcode: String) {
        viewModelScope.launch {

            _productUiState.value = ProductUiState.Loading

            try {
                _productUiState.value = ProductUiState.Success(repository.getProduct(barcode))

            } catch (e: Exception) {
                _productUiState.value =
                    ProductUiState.Error(e.message ?: "Unknown Error occurred")
            }

        }
    }

}
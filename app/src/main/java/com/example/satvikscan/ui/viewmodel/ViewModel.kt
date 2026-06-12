package com.example.satvikscan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satvikscan.Api_Calling.RetrofitInstance
import com.example.satvikscan.data.model.ProductResponse
import com.example.satvikscan.data.repository.ProductRepository
import com.example.satvikscan.ui.IngredientData
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

    // for ingrediant
    private val _ingredients = MutableStateFlow<List<IngredientData>>(emptyList())
    val ingredients: StateFlow<List<IngredientData>> = _ingredients


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
        _productUiState.value = ProductUiState.Idle // reset status to idel
        _ingredients.value = emptyList() // Clear previous ingredients
    }

    fun ChangeproductUiState(barcode: String) {
        viewModelScope.launch {

            _productUiState.value = ProductUiState.Loading

            try {
                val response =
                    repository.getProduct(barcode) // return the dataclass object (json->kotlin object )

                //Ingredients text parse and restricted items take
                val rawIngredientData = response.product?.ingredientsText
                val parsedList = parseIngredients(rawIngredientData)


                _productUiState.value = ProductUiState.Success(response)
                _ingredients.value

            } catch (e: Exception) {
                _productUiState.value = ProductUiState.Error(e.message ?: "Unknown Error occurred")
            }

        }
    }

    private fun parseIngredients(rawIngredientData: String?): List<IngredientData> {

        // check the list are empty or not if emptry so return the empty list
        if (rawIngredientData.isNullOrBlank()) return emptyList()

        // filter the string and an split them
        val rawList =
            rawIngredientData.split(",", ";", "\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

        // store the finding ingredentsn
        val DetectedItems = mutableListOf<IngredientData>()

        // check avalble items are restricted ? using the loop`

    }
}
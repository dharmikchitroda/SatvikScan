package com.example.satvikscan.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.satvikscan.Api_Calling.RetrofitInstance
import com.example.satvikscan.data.Local.PreferenceManager
import com.example.satvikscan.data.model.ProductResponse
import com.example.satvikscan.data.repository.ProductRepository
import com.example.satvikscan.ui.IngredientData
import com.example.satvikscan.ui.IngredientType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Satvik_ViewModel(application: Application) : AndroidViewModel(application) {

    // make PreferenceManager object
    private val prefManager = PreferenceManager(application)

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

    // load diet name
    private val _dietName = MutableStateFlow(prefManager.getdiet() ?: "Swaminarayan")
    val dietName: StateFlow<String> = _dietName

    // load Custom diet name
    private val _customAvoidList = MutableStateFlow(prefManager.getCustomExclusions())
    val customAvoidList: StateFlow<Set<String>> = _customAvoidList

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

                _ingredients.value = parsedList
                _productUiState.value = ProductUiState.Success(response)

            } catch (e: Exception) {
                _productUiState.value = ProductUiState.Error(e.message ?: "Unknown Error occurred")
            }

        }
    }

    private fun parseIngredients(rawIngredientData: String?): List<IngredientData> {

        // check the list are empty or not if empty so return the empty list
        if (rawIngredientData.isNullOrBlank()) return emptyList()

        // filter the string and split them
        val rawList = rawIngredientData.split(",", ";", "\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        // store the finding ingredients
        val DetectedItems = mutableListOf<IngredientData>()

        // check available items are restricted using the loop
        for (ingredient in rawList) {
            val nameLower = ingredient.lowercase()

            // Swaminarayan diet according restricted keywords check
            val isGarlic =
                nameLower.contains("garlic") || nameLower.contains("lasan") || nameLower.contains("lahsun")
            val isOnion =
                nameLower.contains("onion") || nameLower.contains("dungri") || nameLower.contains("pyaj") || nameLower.contains(
                    "pyaz"
                )


            val isRootVegetable =
                isGarlic || isOnion || nameLower.contains("root") || nameLower.contains("radish") || nameLower.contains(
                    "beetroot"
                ) || nameLower.contains("sweet potato")

            val isNonVeg =
                nameLower.contains("chicken") || nameLower.contains("meat") || nameLower.contains("egg") ||
                        nameLower.contains("fish") || nameLower.contains("gelatin") || nameLower.contains(
                    "gelatine"
                ) ||
                        nameLower.contains("beef") || nameLower.contains("pork") || nameLower.contains(
                    "mutton"
                ) ||
                        nameLower.contains("lard") || nameLower.contains("albumin")

            if (isRootVegetable) {
                val displayName = when {
                    isGarlic -> "Garlic"
                    isOnion -> "Onion"
                    else -> ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
                DetectedItems.add(
                    IngredientData(
                        name = displayName,
                        reason = "Avoided: Root vegetables are restricted in Swaminarayan diet",
                        type = IngredientType.ROOT_VEGETABLE
                    )
                )
            } else if (isNonVeg) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Non-vegetarian origin ingredient detected",
                        type = IngredientType.NON_VEG_ORIGIN
                    )
                )
            }
        }
        return DetectedItems
    }

    // when user changes deit
    fun updateDiet(diet: String) {
        _dietName.value = diet
        prefManager.SaveDiet(diet)
    }

    fun addCustomAvoidIngredient(ingredient: String) {
        val clean = ingredient.trim().lowercase()
        if (clean.isEmpty()) return // here stop the fun when empty

        val currentSet = _customAvoidList.value.toMutableSet()
        // currentSet me ae add karenga already nahi hain to true and added
        if (currentSet.add(clean)) {
            _customAvoidList.value = currentSet
            prefManager.saveCustomExclusions(currentSet)
        }

        fun removeCustomAvoidIngredient(ingredient: String) {
            val clean = ingredient.trim().lowercase()
            val currentSet = _customAvoidList.value.toMutableSet()
            if (currentSet.remove(clean)) {
                _customAvoidList.value = currentSet
                prefManager.saveCustomExclusions(currentSet)
            }
        }
    }
}
package com.example.satvikscan.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.satvikscan.Api_Calling.RetrofitInstance
import com.example.satvikscan.data.Local.AppDatabase
import com.example.satvikscan.data.Local.PreferenceManager
import com.example.satvikscan.data.Local.ScanHistoryEntity
import com.example.satvikscan.data.model.Product
import com.example.satvikscan.data.model.ProductResponse
import com.example.satvikscan.data.repository.ProductRepository
import com.example.satvikscan.ui.IngredientData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScanMode {
    BARCODE,
    OCR
}

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

    // for Roomdatabase
    private val database_obj = AppDatabase.getDatabase(application)
    private val scanHistoryDao_obj = database_obj.scanHistoryDao()

    // StateFlow to observe scan history updates in UI
    val scanHistory: StateFlow<List<ScanHistoryEntity>> = scanHistoryDao_obj.getAllScans().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // which open camera according the ocr/scanning
    private val _Scanmode = MutableStateFlow(ScanMode.BARCODE)
    val Scanmode: StateFlow<ScanMode> = _Scanmode

    sealed interface ProductUiState {

        data object Idle : ProductUiState
        data object Loading : ProductUiState

        data object ProductNotFound : ProductUiState
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

                // product not found state
                if (response.status == 0 || response.product == null) {
                    _productUiState.value = ProductUiState.ProductNotFound
                } else {
                    //Ingredients text parse and restricted items take
                    val rawIngredientData = response.product?.ingredientsText

                    val FilteredList = IngredientsFilter(
                        rawIngredientData = rawIngredientData,
                        currentDiet = _dietName.value,
                        customExclusions = _customAvoidList.value
                    )

                    _ingredients.value = FilteredList
                    _productUiState.value = ProductUiState.Success(response)

                    // save the result in history screen using of Room
                    val name = response.product?.productName ?: "Unknown Product"
                    val rawIngredients = response.product?.ingredientsText ?: ""
                    val isSafe = FilteredList.isEmpty()

                    viewModelScope.launch {
                        scanHistoryDao_obj.insertScan(
                            ScanHistoryEntity(
                                barcode = barcode,
                                productName = name,
                                ingredientsText = rawIngredients,
                                isSatvik = isSafe,
                                timestamp = System.currentTimeMillis(),
                                dietName = _dietName.value,
                                imageUrl = response.product?.imageUrl
                            )
                        )

                    }
                }


            } catch (e: Exception) {
                _productUiState.value = ProductUiState.Error(e.message ?: "Unknown Error occurred")
            }

        }
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
    }

    fun removeCustomAvoidIngredient(ingredient: String) {
        val clean = ingredient.trim().lowercase()
        val currentSet = _customAvoidList.value.toMutableSet()
        if (currentSet.remove(clean)) {
            _customAvoidList.value = currentSet
            prefManager.saveCustomExclusions(currentSet)
        }
    }

    // check app first open and check welcome screen open or homescreen
    fun hasSelectedDiet(): Boolean {
        return prefManager.getdiet() != null // dont should be null
    }

    // delete history using room
    fun deleteScan(barcode: String) {
        viewModelScope.launch {
            scanHistoryDao_obj.deleteScan(barcode)
        }
    }

    //Clear History
    fun clearAllScans() {
        viewModelScope.launch {
            scanHistoryDao_obj.clearAllScans()
        }
    }

    fun ChnageScanMode(mode: ScanMode) {
        _Scanmode.value = mode
    }

    fun startOcrScanning() {
        _productUiState.value = ProductUiState.Loading
    }

    // for ocr features
    fun processOcrText(detectedText: String) {
        viewModelScope.launch {
            // _productUiState.value = ProductUiState.Loading

            try {
                if (detectedText.isBlank()) {
                    _productUiState.value =
                        ProductUiState.Error("No text found in the image. Please try again.")
                    return@launch
                } else {

                    val FilteredList = IngredientsFilter(
                        rawIngredientData = detectedText,
                        currentDiet = _dietName.value,
                        customExclusions = _customAvoidList.value
                    )

                    _ingredients.value = FilteredList

                    val productResponse = ProductResponse(
                        code = "OCR_${System.currentTimeMillis()}",
                        status = 1,
                        product = Product(
                            productName = "OCR Scanned Packet",
                            ingredientsText = detectedText,
                            imageUrl = null,
                            imageThumbUrl = null,
                            imageSmallUrl = null
                        )
                    )

                    _productUiState.value = ProductUiState.Success(productResponse)

                    // save in roomDb
                    scanHistoryDao_obj.insertScan(
                        ScanHistoryEntity(
                            barcode = "OCR_${System.currentTimeMillis()}", // unique ID
                            productName = "OCR Scanned Packet",
                            ingredientsText = detectedText,
                            isSatvik = FilteredList.isEmpty(),
                            timestamp = System.currentTimeMillis(),
                            dietName = _dietName.value,
                            imageUrl = null  // OCR dont have an image
                        )
                    )
                }
            } catch (e: Exception) {
                _productUiState.value = ProductUiState.Error(e.message ?: "Unknown Error occurred")
            }
        }
    }

    fun setOcrError(message: String) {
        _productUiState.value = ProductUiState.Error(message)
    }

}

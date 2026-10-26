package com.example.satvikscan.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
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
import java.io.IOException
import java.net.UnknownHostException
import java.util.Locale

enum class ScanMode {
    BARCODE,
    OCR
}

class SatvikViewModel(application: Application) : AndroidViewModel(application) {

    private var lastScannedBarcode: String? = null

    fun isNetworkAvailable(): Boolean {

        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun retryLastScan(onNoBarcodeAndHasInternet: () -> Unit) {
        if (!isNetworkAvailable()) {
            _productUiState.value = ProductUiState.Error("NO_INTERNET")
            return
        }
        val barcode = lastScannedBarcode
        if (barcode != null) {
            changeProductUiState(barcode)
        } else {
            startScanning()
            onNoBarcodeAndHasInternet()
        }
    }

    // make PreferenceManager object
    private val prefManager = PreferenceManager(application)

    // for this portion permission get or not
    private val _cameraPermission = MutableStateFlow(false)
    val cameraPermission: StateFlow<Boolean> = _cameraPermission

    init {
        checkCameraPermission()
    }


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

    // Supported languages in this app
    private val supportedLanguages = listOf("en", "hi", "gu")

    // load app language — smart init
    private val _appLanguage = MutableStateFlow(resolveInitialLanguage())
    val appLanguage: StateFlow<String> = _appLanguage

    /**
     * Decides which language to use:
     * - First install (nothing saved) → auto-detect from phone language
     * - Returning user (saved in prefs) → use saved preference
     */
    private fun resolveInitialLanguage(): String {
        // get from we saved in preferce
        val saved = prefManager.getLanguage()

        return if (saved.isNotEmpty()) {
            // User already selected a language before → respect their choice
            saved
        } else {
            // First time install → detect phone language
            val phoneLang = Locale.getDefault().language // return  "gu", "hi", "en"
            val detected = if (phoneLang in supportedLanguages) phoneLang
            else "en"
            // Save it so we don't auto-detect again next time
            prefManager.saveLanguage(detected)
            detected
        }
    }

    // for Roomdatabase
    private val database_obj = AppDatabase.getDatabase(application)
    private val scanHistoryDao = database_obj.scanHistoryDao()

    // StateFlow to observe scan history updates in UI
    val scanHistory: StateFlow<List<ScanHistoryEntity>> = scanHistoryDao.getAllScans().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //  open camera according mode like ocr/scanning
    private val _scanMode = MutableStateFlow(ScanMode.BARCODE)
    val scanMode: StateFlow<ScanMode> = _scanMode

    // use for scan unsupported barcode
    private val _barcodeError = MutableStateFlow<String?>(null)
    val barcodeError: StateFlow<String?> = _barcodeError

    sealed interface ProductUiState {

        data object Idle : ProductUiState
        data object Loading : ProductUiState

        data object ProductNotFound : ProductUiState
        data class Success(val product: ProductResponse) :
            ProductUiState // api respons data as actual object provoded by retrofit

        data class Error(val message: String) : ProductUiState
    }

    fun checkCameraPermission() {
        val isGranted = ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        _cameraPermission.value = isGranted
    }

    fun onPermissionResult(isGranted: Boolean) {
        _cameraPermission.value = isGranted
    }

    fun stopScanning() {
        _isScanning.value = false
    }

    fun startScanning() {
        _isScanning.value = true
        _productUiState.value = ProductUiState.Idle // reset status to idel
        _ingredients.value = emptyList() // Clear previous ingredients
    }

    fun changeProductUiState(barcode: String) {

        lastScannedBarcode = barcode

        viewModelScope.launch {

            _productUiState.value = ProductUiState.Loading

            if (!isNetworkAvailable()) {
                _productUiState.value = ProductUiState.Error("NO_INTERNET")
                return@launch
            }

            try {
                val response =
                    repository.getProduct(barcode) // return the dataclass object (json->kotlin object )

                // product not found state
                if (response.status == 0 || response.product == null) {

                    _productUiState.value = ProductUiState.ProductNotFound

                } else {
                    //Ingredients text parse and restricted items take
                    val rawIngredientData = response.product?.ingredientsText

                    val filteredList = ingredientsFilter(
                        rawIngredientData = rawIngredientData,
                        currentDiet = _dietName.value,
                        customExclusions = _customAvoidList.value
                    )

                    _ingredients.value = filteredList
                    _productUiState.value = ProductUiState.Success(response)

                    // save the result in history screen using of Room
                    val name = response.product?.productName ?: "Unknown Product"
                    val rawIngredients = response.product?.ingredientsText ?: ""
                    val isSafe = filteredList.isEmpty()

                    viewModelScope.launch {
                        scanHistoryDao.insertScan(
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
                if (e is IOException ) {
                    _productUiState.value = ProductUiState.Error("NO_INTERNET")
                } else {
                    _productUiState.value =
                        ProductUiState.Error(e.message ?: "Unknown Error occurred")
                }
            }

        }
    }


    // when user changes deit
    fun updateDiet(diet: String) {
        _dietName.value = diet
        prefManager.SaveDiet(diet)
    }

    // when user manually changes app language from the in-app button
    fun updateLanguage(lang: String) {
        _appLanguage.value = lang
        prefManager.saveLanguage(lang)
        // Apply locale immediately — Android strings switch without restart
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(lang)
        )
    }

    // Called once from MainActivity before UI loads — applies the resolved language
    fun applyStoredLanguage() {
        val lang = _appLanguage.value
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(lang)
        )
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

    // check app first open and check onboarding open or homescreen
    fun hasSelectedDiet(): Boolean {
        return prefManager.getdiet() != null
    }

    fun isOnboardingCompleted(): Boolean {
        return prefManager.isOnboardingCompleted() && prefManager.getdiet() != null
    }

    fun completeOnboarding() {
        prefManager.setOnboardingCompleted(true)
    }

    fun resetAppOnboarding() {
        prefManager.clearAllPreferences()
        _dietName.value = "Swaminarayan"
        _customAvoidList.value = emptySet()
    }

    // delete history using room
    fun deleteScan(barcode: String) {
        viewModelScope.launch {
            scanHistoryDao.deleteScan(barcode)
        }
    }

    //Clear History
    fun clearAllScans() {
        viewModelScope.launch {
            scanHistoryDao.clearAllScans()
        }
    }

    fun changeScanMode(mode: ScanMode) {
        _scanMode.value = mode
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

                    val filteredList = ingredientsFilter(
                        rawIngredientData = detectedText,
                        currentDiet = _dietName.value,
                        customExclusions = _customAvoidList.value
                    )

                    _ingredients.value = filteredList

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

                    // Calculate sequential index based on current saved OCRscans

                    val count = scanHistory.value.count {
                        it.imageUrl.isNullOrEmpty() && it.isSatvik == filteredList.isEmpty()
                    }
                    val allocatedIndex = count % 3

                    scanHistoryDao.insertScan(
                        ScanHistoryEntity(
                            barcode = "OCR_${System.currentTimeMillis()}", // unique ID
                            productName = "Scanned Packet",
                            ingredientsText = detectedText,
                            isSatvik = filteredList.isEmpty(),
                            timestamp = System.currentTimeMillis(),
                            dietName = _dietName.value,
                            imageUrl = null, // OCR dont have an image
                            imageIndex = allocatedIndex
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

    fun setBarcodeError(message: String?) {
        _barcodeError.value = message
    }

    fun clearBarcodeError() {
        _barcodeError.value = null
    }

}

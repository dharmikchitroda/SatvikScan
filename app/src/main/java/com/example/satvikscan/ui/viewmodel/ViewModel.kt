package com.example.satvikscan.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Satvik_ViewModel : ViewModel() {
    // for this portion permission get or not
    private val _CameraPermission = MutableStateFlow(false)
    val CameraPermission: StateFlow<Boolean> = _CameraPermission

    fun PermissionResult(isGranted: Boolean) {
        _CameraPermission.value = isGranted
    }
}
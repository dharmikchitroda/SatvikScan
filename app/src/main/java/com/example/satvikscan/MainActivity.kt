package com.example.satvikscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.satvikscan.ui.BarcodeScannerScreen
import com.example.satvikscan.ui.CommonUi
import com.example.satvikscan.ui.theme.SatvikScanTheme
import com.example.satvikscan.ui.theme.Satvik_ViewModel

class MainActivity : ComponentActivity() {
    private val viewModel : Satvik_ViewModel  by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatvikScanTheme {
//                CommonUi()
                BarcodeScannerScreen(viewModel)

            }
        }
    }
}


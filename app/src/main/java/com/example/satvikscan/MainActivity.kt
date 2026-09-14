package com.example.satvikscan

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.satvikscan.ui.CommonUi
import com.example.satvikscan.ui.theme.SatvikScanTheme
import com.example.satvikscan.viewmodel.SatvikViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: SatvikViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Applies the initial detected/saved language on app startup
        viewModel.applyStoredLanguage()

        setContent {
            SatvikScanTheme {
                CommonUi()
            }
        }
    }
}

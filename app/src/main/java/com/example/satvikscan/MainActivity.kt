package com.example.satvikscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.satvikscan.ui.CommonUi
import com.example.satvikscan.ui.ResultScreen
import com.example.satvikscan.ui.ResultScreenPreview
import com.example.satvikscan.ui.theme.SatvikScanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatvikScanTheme {
          CommonUi()
//                ResultScreenPreview()

            }
        }
    }
}


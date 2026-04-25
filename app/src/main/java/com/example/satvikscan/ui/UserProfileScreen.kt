package com.example.satvikscan.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun UserProfileScreen(paddingValues: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(paddingValues)){
        Text("this is user profile screen ")
    }
}
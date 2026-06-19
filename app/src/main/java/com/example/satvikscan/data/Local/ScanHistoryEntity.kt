package com.example.satvikscan.data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey 
    val barcode: String,
    val productName: String,
    val ingredientsText: String,
    val isSatvik: Boolean,
    val timestamp: Long
)

package com.example.satvikscan.data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // replace row when already exist row without this compiler confuse - eror du ya repalce kar du
    suspend fun insertScan( scan: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanHistoryEntity>> // work with flow dynamically

    @Query("DELETE FROM scan_history WHERE barcode = :barcode")
    suspend fun deleteScan(barcode: String)

    @Query("DELETE FROM scan_history")
    suspend fun clearAllScans()
}

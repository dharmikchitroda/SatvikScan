package com.example.satvikscan.data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScanHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

       // synchronized(this) used to multithreading enter one thred in one time
            // Also can use  if (INSTANCE == null) {}
            return INSTANCE ?: synchronized(this) {  // return INSTANCE if not null and when is null ( ?: )make a object first time and return

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "satvik_scan_database"
                    ).build()

                    INSTANCE = instance
                    instance
                }
        }
    }
}

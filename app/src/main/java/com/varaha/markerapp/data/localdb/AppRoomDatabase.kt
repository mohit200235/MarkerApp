package com.varaha.markerapp.data.localdb


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarkerData::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun markerDataDao(): MarkerDataDao
}
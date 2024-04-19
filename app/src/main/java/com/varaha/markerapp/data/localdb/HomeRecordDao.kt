package com.varaha.markerapp.data.localdb


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDataDao {
    @Query("SELECT * FROM MARKERDATA")
    fun getAll(): Flow<List<MarkerData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(markerData: MarkerData)

    @Delete
    fun delete(markerData : MarkerData)
}
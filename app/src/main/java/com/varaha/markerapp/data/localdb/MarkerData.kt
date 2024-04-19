package com.varaha.markerapp.data.localdb


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "relation") val relation: String?,
    @ColumnInfo(name = "age") val age: String,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)
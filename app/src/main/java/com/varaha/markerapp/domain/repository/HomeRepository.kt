package com.varaha.markerapp.domain.repository

import com.varaha.markerapp.data.localdb.MarkerData
import kotlinx.coroutines.flow.Flow

abstract class HomeRepository {
    abstract fun getAllMarkerFromLocalDb() : Flow<List<MarkerData>>
    abstract fun saveMarkerToLocalDb(markerData: MarkerData)
    abstract fun deleteMarkerFromLocalDb(markerData: MarkerData)
}
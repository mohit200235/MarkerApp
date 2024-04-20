package com.varaha.markerapp.data.repositoryImpl

import com.varaha.markerapp.data.localdb.MarkerData
import com.varaha.markerapp.data.localdb.MarkerDataDao
import com.varaha.markerapp.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val markerDataDao: MarkerDataDao
) : HomeRepository() {
    override fun getAllMarkerFromLocalDb(): Flow<List<MarkerData>>  {
        return markerDataDao.getAll()
    }
    override fun saveMarkerToLocalDb(markerData: MarkerData){
        markerDataDao.insert(markerData)
    }

    override fun deleteMarkerFromLocalDb(markerData: MarkerData) {
        markerDataDao.delete(markerData)
    }

    override fun updateMarkerToLocalDb(markerData: MarkerData) {
        markerDataDao.update(markerData)
    }
}
package com.varaha.markerapp.presentation.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varaha.markerapp.data.localdb.MarkerData
import com.varaha.markerapp.domain.repository.HomeRepository
import com.varaha.markerapp.domain.responseState.ResponseState
import com.varaha.markerapp.presentation.ui.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private var repository: HomeRepository,
    private var handler: CoroutineExceptionHandler,
) : ViewModel() {

    private var _getMarkerDataFromLocalDb = MutableStateFlow(HomeState())
    val getMarkerDataFromLocalDb: StateFlow<HomeState>
        get() = _getMarkerDataFromLocalDb

    private val _markerAddressDetail = MutableStateFlow<ResponseState<Address>>(ResponseState.Idle)
    val markerAddressDetail = _markerAddressDetail.asStateFlow()

    fun getMarkerAddressDetails(lat: Double, long: Double, context: Context) {
        _markerAddressDetail.value = ResponseState.Loading
        try {

            val geocoder = Geocoder(context, Locale.getDefault())
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    lat,
                    long,
                    1,
                ) { p0 ->
                    _markerAddressDetail.value = ResponseState.Success(p0[0])
                }
            } else {
                val addresses = geocoder.getFromLocation(
                    lat,
                    long,
                    1,
                )
                _markerAddressDetail.value =
                    if(!addresses.isNullOrEmpty()){
                        ResponseState.Success(addresses[0])
                    }else{
                        ResponseState.Error(Exception("Address is null"))
                    }
            }
        } catch (e: Exception) {
            _markerAddressDetail.value = ResponseState.Error(e)
        }
    }

    fun resetMarkerAddressDetail() {
        _markerAddressDetail.value = ResponseState.Idle
    }

    fun getAllDataFromLocalDb() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _getMarkerDataFromLocalDb.update {
                it.copy(isLoading = true)
            }
            repository.getAllMarkerFromLocalDb().collectLatest { updatedList ->
                _getMarkerDataFromLocalDb.update {
                    it.copy(
                        isLoading = false,
                        list = updatedList
                    )
                }
            }
        }
    }

    fun deleteMarkerFromDb(markerData: MarkerData){
        viewModelScope.launch ( Dispatchers.IO ){
            repository.deleteMarkerFromLocalDb(markerData)
        }
    }

    fun saveLatLongToLocalDb(
        name: String,
        city: String,
        relation: String,
        age: String,
        address: String,
        latLong: String
    ) {
        val stringArray = latLong.split(",")
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveMarkerToLocalDb(
                MarkerData(
                    name = name,
                    city = city,
                    relation = relation,
                    age = age,
                    address = address,
                    latitude = stringArray[0],
                    longitude = stringArray[1],
                )
            )
        }
    }
}
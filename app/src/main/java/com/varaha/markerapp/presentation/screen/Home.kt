package com.varaha.markerapp.presentation.screen

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.varaha.markerapp.data.localdb.MarkerData
import com.varaha.markerapp.domain.responseState.ResponseState
import com.varaha.markerapp.presentation.component.AlertDialogComponent
import com.varaha.markerapp.presentation.component.DialogDataShow
import com.varaha.markerapp.presentation.ui.state.HomeState
import com.varaha.markerapp.presentation.viewmodel.HomeViewModel


@Composable
fun HomeScreenRoute(
    context: Context = LocalContext.current,
    viewModel: HomeViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
        viewModel.getAllDataFromLocalDb()
    }

    val localDbState by viewModel.getMarkerDataFromLocalDb.collectAsState()
    val addressState by viewModel.markerAddressDetail.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val openShowDataDialog = remember { mutableStateOf(false) }
    var MarkerData = remember { mutableStateOf<MarkerData?>(null)    }
    val latLong = remember { mutableStateOf("") }
    val addressToShow = remember { mutableStateOf("")    }

    if(localDbState.isLoading){
        return
    }

    HomeScreen(
        localDbState,
        onMarkerClick = {
            Log.d("TAG", "HomeScreenRoute: clicked $it" )
            MarkerData.value = it
            openShowDataDialog.value = true
            //On Clicking the marker
        },
        onMapClick = {
            //On clicking the map get the lat long of point clicked
            latLong.value = "${it.latitude},${it.longitude}"
            viewModel.getMarkerAddressDetails(it.latitude, it.longitude, context)
            Log.d("TAG", "HomeScreenRoute: " + latLong)
            //openAlertDialog.value = true
        }
    )

    if(addressState is ResponseState.Success) {
        val address = (addressState as ResponseState.Success<Address>).data
        addressToShow.value = address.locality +" "+ address.subAdminArea
        Log.d("TAG", "HomeScreenRoute: $addressToShow ")
        openAlertDialog.value = true
    }

    if(openAlertDialog.value){
        AlertDialogComponent(
            onDismissRequest = {
                openAlertDialog.value = false
                viewModel.resetMarkerAddressDetail()
            },
            viewModel,
            onConfirmation = {
//                viewModel.saveLatLongToLocalDb("","","",5,addressToShow.value,latLong.value)
                openAlertDialog.value = false
                viewModel.resetMarkerAddressDetail()
            },
            latLong.value,
            addressToShow.value
        )
    }


    if(openShowDataDialog.value){
        DialogDataShow(
            MarkerData.value!!,
            onDismissRequest = {
                openShowDataDialog.value = false
            },
            onConfirmDelete = {
                openShowDataDialog.value = false
                viewModel.deleteMarkerFromDb(MarkerData.value!!)
            },
        )
    }
}

@Composable
fun HomeScreen(
    localDbState: HomeState,
    onMarkerClick :(MarkerData) -> Unit,
    onMapClick: (LatLng) -> Unit
) {

    val defaultIndiaLocation = LatLng(23.473324, 77.947998)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultIndiaLocation, 5f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            onMapClick.invoke(it)
        }
    ){
        localDbState.list.forEach { item ->
            Marker(
                state = MarkerState(position = LatLng(item.latitude!!.toDouble(), item.longitude!!.toDouble())),
                title = item.city,
                onClick = {
                    onMarkerClick.invoke(item)
                    true
                }
            )
        }
    }
}
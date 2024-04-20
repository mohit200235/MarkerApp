package com.varaha.markerapp.presentation.screen

import android.content.Context
import android.location.Address
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.varaha.markerapp.R
import com.varaha.markerapp.data.localdb.MarkerData
import com.varaha.markerapp.domain.responseState.ResponseState
import com.varaha.markerapp.presentation.component.DialogDataShow
import com.varaha.markerapp.presentation.ui.state.HomeState
import com.varaha.markerapp.presentation.viewmodel.HomeViewModel


@Composable
fun HomeScreenRoute(
    context: Context = LocalContext.current,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getAllDataFromLocalDb()
    }

    val localDbState by viewModel.getMarkerDataFromLocalDb.collectAsState()
    val addressState by viewModel.markerAddressDetail.collectAsState()
    val openShowDataDialog = remember { mutableStateOf(false) }

    val MarkerData = remember { mutableStateOf<MarkerData?>(null) }
    val latLong = remember { mutableStateOf("") }
    val addressToShow = remember { mutableStateOf("") }
    val showPreview = remember { mutableStateOf(false) }

    if (localDbState.isLoading) {
        return
    }

    HomeScreen(
        localDbState,
        viewModel,
        onMarkerClick = {
            MarkerData.value = it
            openShowDataDialog.value = true
        },
        onMapClick = {
            //On clicking the map get the lat long of point clicked
            latLong.value = "${it.latitude},${it.longitude}"
            viewModel.getMarkerAddressDetails(it.latitude, it.longitude, context)
            showPreview.value = true
        }
    )


    //show marker when w have the address from lat long
    if (addressState is ResponseState.Success && showPreview.value) {
        val address = (addressState as ResponseState.Success<Address>).data
        addressToShow.value = address.locality + " " + address.subAdminArea
        Log.d("TAG", "HomeScreenRoute: $addressToShow ")
        viewModel.saveLatLongToLocalDb("", "", "", "", addressToShow.value, latLong.value)
        showPreview.value = false
    }

    //check when the dialog value is true
    if (openShowDataDialog.value) {
        DialogDataShow(
            MarkerData.value!!,
            viewModel,
            onDismissRequest = {
                openShowDataDialog.value = false
            },
            onDismissUpdate = {
                openShowDataDialog.value = false
            },
        )
    }
}

@Composable
fun HomeScreen(
    localDbState: HomeState,
    viewModel: HomeViewModel,
    onMarkerClick: (MarkerData) -> Unit,
    onMapClick: (LatLng) -> Unit
) {
    //get the current context
    val context: Context = LocalContext.current

    val defaultIndiaLocation = LatLng(23.473324, 77.947998)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultIndiaLocation, 5f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                onMapClick.invoke(it)
            }
        ) {
            localDbState.list.forEach { item ->
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            item.latitude!!.toDouble(),
                            item.longitude!!.toDouble()
                        )
                    ),

                    //change the values and icon when marker is saved

                    title = if (!item.name.isNullOrBlank()) "Name : " + item.name else "Address : " + item.address,
                    snippet = if (!item.name.isNullOrBlank()) "Relation : " + item.relation else "Lat : " + item.latitude.toFloat() + " Long : " + item.longitude,
                    icon = if (item.name.isNullOrBlank())
                        //get the other icon when marker not saved
                        viewModel.bitmapDescriptor(context, R.drawable.location)
                    else null,
                    //show the info window on click
                    onInfoWindowClick = {
                        onMarkerClick.invoke(item)
                    }
                )
            }
        }
    }
}

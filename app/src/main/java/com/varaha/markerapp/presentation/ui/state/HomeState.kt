package com.varaha.markerapp.presentation.ui.state

import com.varaha.markerapp.data.localdb.MarkerData

data class HomeState(
    val isLoading: Boolean = false,
    val openAlertDialog : Boolean = false,
    val list: List<MarkerData> = emptyList()
)
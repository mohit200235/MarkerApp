package com.varaha.markerapp.domain.responseState

sealed class ResponseState<out T> {
    data object Idle : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
    data class Error(val error: Throwable) : ResponseState<Nothing>()
    data class Success<R>(val data: R) : ResponseState<R>()
}
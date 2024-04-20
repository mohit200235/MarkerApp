package com.varaha.markerapp.presentation.component

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable


@Composable
fun OpenDeleteConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Log.d("TAG", "OpenDeleteConfirmDialog: ")
            onConfirmDelete()
        }
    )
}

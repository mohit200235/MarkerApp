package com.varaha.markerapp.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.varaha.markerapp.data.localdb.MarkerData
import com.varaha.markerapp.presentation.viewmodel.HomeViewModel

@Composable
fun DialogDataShow(
    markerData: MarkerData,
    viewModel: HomeViewModel,
    onDismissRequest: () -> Unit,
    onDismissUpdate: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        content = {
            CustomShowDialog(
                markerData,
                viewModel,
                onDismissRequest,
                onDismissUpdate,
            )
        },
    )
}

@Composable
fun CustomShowDialog(
    markerData: MarkerData,
    viewModel: HomeViewModel,
    onDismissRequest: () -> Unit,
    onDismissUpdate: () -> Unit,
) {

    val openDeleteConfirmDialog = remember { mutableStateOf(false) }

    var name by remember {
        mutableStateOf(markerData.name ?: "")
    }
    var city by remember {
        mutableStateOf(markerData.city ?: "")
    }
    var relation by remember {
        mutableStateOf(markerData.relation ?: "")
    }
    var age by remember {
        mutableStateOf(markerData.age ?: "")
    }

    if (openDeleteConfirmDialog.value) {
        OpenDeleteConfirmDialog(
            onDismissRequest = {
                openDeleteConfirmDialog.value = false
            },
            onConfirmDelete = {
                openDeleteConfirmDialog.value = false
                viewModel.deleteMarkerFromDb(markerData)
            }
        )
    }

    Column(
        modifier = Modifier.background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Details",
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )


        TextField(
            value = name, onValueChange = { name = it },
            label = { Text("Enter name", color = Color.Black) },
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp),
            maxLines = 1
        )

        TextField(
            value = city, onValueChange = { city = it },
            label = { Text("Enter city") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            maxLines = 1

        )

        TextField(
            value = relation, onValueChange = { relation = it },
            label = { Text("Your relation") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            maxLines = 1
        )

        TextField(
            value = age, onValueChange = { age = it },
            label = { Text("Enter age") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1
        )
        markerData.address?.let {
            TextField(
                value = it, onValueChange = { },
                label = { Text("Address is :") },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                maxLines = 2,
                enabled = false
            )
        }
        TextField(
            value = markerData.latitude + " " + markerData.longitude, onValueChange = {},
            label = { Text("Latitute & Longitude :") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            maxLines = 2,
            enabled = false
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    openDeleteConfirmDialog.value = true
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            ) {
                Text(text = "Delete")
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {
                    onDismissUpdate()
                    viewModel.updateMarkerDb(
                        markerData.id,
                        name,
                        city,
                        relation,
                        age,
                        markerData.address.toString(),
                        markerData.latitude + "," + markerData.longitude
                    )
                }, modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            ) {
                Text(text = "Update")
            }
        }

    }

}


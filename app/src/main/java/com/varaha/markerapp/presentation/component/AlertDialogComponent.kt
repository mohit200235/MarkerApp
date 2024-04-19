package com.varaha.markerapp.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import com.varaha.markerapp.presentation.viewmodel.HomeViewModel

@Composable
fun AlertDialogComponent(
    onDismissRequest: () -> Unit,
//    onDeleteRequest : () ->Unit,
    viewModel: HomeViewModel,
    onConfirmation: () -> Unit,
    latLng: String,
    address: String
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        content = {
            CustomAlertDialog(
                onDismissRequest,
                viewModel,
                onConfirmation,
                latLng,
                address
            )
        },
    )
}

@Composable
fun CustomAlertDialog(

    onDismissRequest: () -> Unit,
    viewModel: HomeViewModel,
    onConfirmation: () -> Unit,
    latLng: String,
    address: String
) {
    var name by remember {
        mutableStateOf("")
    }
    var city by remember {
        mutableStateOf("")
    }
    var relation by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter Details",
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
        TextField(
            value = address, onValueChange = { },
            label = { Text("Address is :") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            maxLines = 2,
            enabled = false
        )
        TextField(
            value = latLng, onValueChange = {},
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
                onClick = { onDismissRequest() }, modifier = Modifier
                    .weight(1F)
                    .padding(end = 5.dp)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = {
                    onConfirmation()
                    Log.d("TAG", "CustomAlertDialog: name is -> " + name)
                    viewModel.saveLatLongToLocalDb(name, city, relation, age, address, latLng)
                }, modifier = Modifier
                    .weight(1F)
                    .padding(start = 5.dp)
            ) {
                Text(text = "Save")
            }
        }

    }
}

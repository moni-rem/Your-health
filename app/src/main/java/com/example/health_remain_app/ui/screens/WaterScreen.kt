package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun WaterScreen(viewModel: HealthViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Add Water 💧",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            viewModel.addWater(200)
        }) {
            Text("+200ml")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            viewModel.addWater(500)
        }) {
            Text("+500ml")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            viewModel.addWater(1000)
        }) {
            Text("+1L")
        }
    }
}
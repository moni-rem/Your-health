package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun HistoryScreen(viewModel: HealthViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Today Summary 📅",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Water Intake: ${viewModel.waterIntake}ml")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Meals Completed:")

                viewModel.meals.forEach {
                    Text(
                        "${it.name}: ${if (it.completed) "Done ✔" else "Pending ❌"}"
                    )
                }
            }
        }
    }
}
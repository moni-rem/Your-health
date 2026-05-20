package com.example.health_remain_app.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HealthViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Dashboard 📊",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Water Intake 💧")

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { viewModel.getProgress() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "${viewModel.waterIntake}ml / ${viewModel.dailyGoal}ml"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("water")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Water")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate("meal")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Meal Tracker")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate("history")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View History")
        }
    }
}
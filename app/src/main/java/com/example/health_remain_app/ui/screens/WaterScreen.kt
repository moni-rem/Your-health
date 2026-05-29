package com.example.health_remain_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.health_remain_app.viewmodel.HealthViewModel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import com.example.health_remain_app.notification.ReminderScheduler

@Composable
fun WaterScreen(
    navController: NavController,
    viewModel: HealthViewModel
) {
    val context = LocalContext.current
    var waterGoal by remember { mutableStateOf("") }
    val drinkOptions = listOf(150, 200, 250, 330, 450, 550)
    var expanded by remember { mutableStateOf(false) }
    var selectedDrinkAmount by remember { mutableIntStateOf(viewModel.defaultDrinkAmount) }

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // TOP HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        brush = Brush.verticalGradient(colors = listOf(blue1, blue2)),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Icon(Icons.Default.WaterDrop, null, tint = Color.White, modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Daily Water Goal", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }

            // CONTENT CARD
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).offset(y = (-40).dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Set Your Goal", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = waterGoal,
                        onValueChange = { waterGoal = it },
                        label = { Text("Enter goal (ml)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Test Notification Button
                    Button(
                        onClick = {
                            ReminderScheduler.scheduleTestReminder(context)
                            Toast.makeText(context, "Testing: Notification in 1 minute...", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.NotificationsActive, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Test 1 Min Notification", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (waterGoal.isNotEmpty()) {
                                viewModel.setWaterGoal(waterGoal.toInt())
                                viewModel.setDefaultDrinkAmount(selectedDrinkAmount)
                                ReminderScheduler.scheduleReminder(context, 60, selectedDrinkAmount)
                                viewModel.saveGoalToFirestore()
                                navController.navigate("home")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = blue2)
                    ) {
                        Text("Save Goal", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

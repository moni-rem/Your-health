package com.example.health_remain_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    var waterGoal by remember {
        mutableStateOf("")
    }

    val drinkOptions =
        listOf(
            150,
            200,
            250,
            330,
            450,
            550
        )

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedDrinkAmount by remember {
        mutableIntStateOf(
            viewModel.defaultDrinkAmount
        )
    }

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    val darkText = Color(0xFF1F2937)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5FBFF),
                        Color(0xFFEAF6FF)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // =========================
            // TOP HEADER
            // =========================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                blue1,
                                blue2
                            )
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = 40.dp,
                            bottomEnd = 40.dp
                        )
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Color.White.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Daily Water Goal",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Stay hydrated everyday",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            // =========================
            // CONTENT CARD
            // =========================

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-40).dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Set Your Goal",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = darkText
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Recommended 2000ml - 3000ml daily",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // INPUT FIELD

                    OutlinedTextField(
                        value = waterGoal,
                        onValueChange = {
                            waterGoal = it
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        placeholder = {

                            Text(
                                text = "Enter water goal",
                                color = Color.Gray
                            )
                        },
                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = blue2
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = blue2,
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedTextColor = darkText,
                            unfocusedTextColor = darkText,
                            cursorColor = blue2
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Default Drink Amount",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = darkText,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedButton(
                            onClick = {
                                expanded = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {

                            Text(
                                text = "$selectedDrinkAmount ml",
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            drinkOptions.forEach { amount ->

                                DropdownMenuItem(
                                    text = {
                                        Text("$amount ml")
                                    },
                                    onClick = {

                                        selectedDrinkAmount = amount

                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(35.dp))

                    // SAVE BUTTON

                    Button(
                        onClick = {

                            if (waterGoal.isNotEmpty()) {

                                viewModel.setWaterGoal(
                                    waterGoal.toInt()
                                )

                                viewModel.setDefaultDrinkAmount(
                                    selectedDrinkAmount
                                )

                                // Start reminder notification

                                ReminderScheduler.scheduleReminder(
                                    context = context,
                                    intervalMinutes = 60, // change later to user selection
                                    amount = selectedDrinkAmount
                                )

                                viewModel.saveGoalToFirestore()

                                Toast.makeText(
                                    context,
                                    "Goal Saved",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate("home") {

                                    popUpTo("home") {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = blue2
                        )
                    ) {

                        Text(
                            text = "Save Goal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Healthy hydration improves focus, energy, and wellness.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
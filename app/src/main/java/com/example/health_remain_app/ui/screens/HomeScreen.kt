package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.health_remain_app.viewmodel.HealthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HealthViewModel
) {

    val user = FirebaseAuth.getInstance().currentUser

    val username =
        user?.displayName ?: "User"

    val todayWater = viewModel.getTodayTotal()

    val goal = viewModel.waterGoal


    val progress =
        (todayWater.toFloat() / goal.toFloat())
            .coerceIn(0f, 1f)

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    val background = Color(0xFFF4FAFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            // =========================
            // TOP SECTION
            // =========================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
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
                        .padding(24.dp)
                ) {

                    // TOP BAR

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = { }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                navController.navigate("profile")
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Text(
                        text = "Hello,",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 18.sp
                    )

                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )

                    // CIRCLE PROGRESS

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(220.dp),
                            strokeWidth = 12.dp,
                            color = Color.White,
                            trackColor =
                                Color.White.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round
                        )

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Achieved goals",
                                color =
                                    Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text =
                                    "${(progress * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "$todayWater ml",
                                color =
                                    Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // =========================
            // CONTENT
            // =========================

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp
                )
            ) {

                Text(
                    text = "Quick Add",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    WaterButton(
                        amount = "220ml",
                        modifier = Modifier.weight(1f)
                    ) {

                        viewModel.drinkWater(
                            viewModel.defaultDrinkAmount
                        )
                    }

                    WaterButton(
                        amount = "330ml",
                        modifier = Modifier.weight(1f)
                    ) {

                        viewModel.drinkWater(
                            viewModel.defaultDrinkAmount
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    WaterButton(
                        amount = "450ml",
                        modifier = Modifier.weight(1f)
                    ) {

                        viewModel.drinkWater(
                            viewModel.defaultDrinkAmount
                        )
                    }

                    WaterButton(
                        amount = "550ml",
                        modifier = Modifier.weight(1f)
                    ) {

                        viewModel.drinkWater(
                            viewModel.defaultDrinkAmount
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // =========================
                // STATS CARD
                // =========================

                // =========================
                // STATS CARD
                // =========================

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        // REMAINING / COMPLETED

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text =
                                    if (todayWater >= goal)
                                        "Completed"
                                    else
                                        "Remaining",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    if (todayWater >= goal)
                                        "Goal Reached 🎉"
                                    else
                                        "${(goal - todayWater).coerceAtLeast(0)}ml",
                                color = Color(0xFF005BEA),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // TARGET

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Daily Goal",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "${goal}ml",
                                color = Color(0xFF005BEA),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }



                // =========================
                // REMINDER CARD
                // =========================

                ReminderTimeCard()

                Spacer(
                    modifier = Modifier.height(120.dp)
                )
            }
        }

        // =========================
        // FLOATING BUTTON
        // =========================

        FloatingActionButton(
            onClick = {

                viewModel.drinkWater(
                    viewModel.defaultDrinkAmount
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .size(75.dp),
            containerColor = blue2,
            shape = CircleShape
        ) {

            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Composable
fun ReminderTimeCard() {

    val reminderOptions =
        listOf(
            "15 min",
            "30 min",
            "1 hour",
            "2 hours",
            "3 hours"
        )

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedReminder by remember {
        mutableStateOf("30 min")
    }

    val blue2 = Color(0xFF005BEA)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFE0F2FE)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Notifications,
                        contentDescription = null,
                        tint = blue2
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column {

                    Text(
                        text = "Water Reminder",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Stay hydrated throughout the day",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Box {

                OutlinedButton(
                    onClick = {

                        expanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = blue2
                        )
                ) {

                    Text(
                        text = selectedReminder,
                        modifier = Modifier.weight(1f),
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Icon(
                        imageVector =
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {

                        expanded = false
                    }
                ) {

                    reminderOptions.forEach { option ->

                        DropdownMenuItem(
                            text = {

                                Text(option)
                            },
                            onClick = {

                                selectedReminder = option

                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaterButton(
    amount: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(90.dp)
            .clickable {

                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = null,
                tint = Color(0xFF005BEA),
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = amount,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }
    }
}
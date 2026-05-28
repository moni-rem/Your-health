package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.health_remain_app.notifications.NotificationWorker
import com.example.health_remain_app.viewmodel.HealthViewModel
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HealthViewModel
) {

    val todayWater = viewModel.getTodayTotal()

    val goal = viewModel.waterGoal

    val progress = (
            todayWater.toFloat() / goal.toFloat()
            ).coerceIn(0f, 1f)

    val fontBlue = Color(0xFF374151) // Deep cobalt blue for the font

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FC))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Hydration Tracker",
                        color = fontBlue,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Stay hydrated everyday",
                        color = fontBlue.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("profile")
                    },
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = fontBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBFDDF0)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "Today's Progress",
                        color = fontBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp),
                        color = fontBlue,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${todayWater}ml / ${goal}ml completed",
                        color = fontBlue,
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                StatCard(
                    title = "Cups",
                    value = "${todayWater}ml",
                    modifier = Modifier.weight(1f),
                    textColor = fontBlue
                )

                StatCard(
                    title = "Goal",
                    value = "${goal}ml",
                    modifier = Modifier.weight(1f),
                    textColor = fontBlue
                )
            }

            ReminderSelector(textColor = fontBlue)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBFDDF0)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = fontBlue,
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "Water Reminder Active",
                            color = fontBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "You'll receive reminders",
                            color = fontBlue.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.drinkWater()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan
                )
            ) {

                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    tint = fontBlue
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "I Drank Water",
                    color = fontBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    textColor: Color
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBFDDF0)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                color = textColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ReminderSelector(textColor: Color) {

    val context = LocalContext.current
    val reminderOptions = listOf(1, 5, 10, 15, 30, 60)
    var expanded by remember { mutableStateOf(false) }
    var selectedMinute by remember { mutableIntStateOf(5) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBFDDF0)
        ),
        shape = RoundedCornerShape(28.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Reminder Interval",
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = textColor
                ),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Every $selectedMinute minutes",
                    modifier = Modifier.weight(1f),
                    color = textColor
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = textColor
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                reminderOptions.forEach { minute ->

                    DropdownMenuItem(
                        text = {
                            Text("$minute minutes", color = textColor)
                        },
                        onClick = {
                            selectedMinute = minute
                            expanded = false

                            val workRequest =
                                PeriodicWorkRequestBuilder<NotificationWorker>(
                                    selectedMinute.toLong(),
                                    TimeUnit.MINUTES
                                ).build()

                            WorkManager.getInstance(context)
                                .enqueueUniquePeriodicWork(
                                    "water_reminder",
                                    ExistingPeriodicWorkPolicy.UPDATE,
                                    workRequest
                                )
                        }
                    )
                }
            }
        }
    }
}

package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HealthViewModel) {
    HomeContent(
        onDrinkWaterClick = { /* Handle via viewModel if needed */ },
        onProfileClick = { /* Navigate if needed */ }
    )
}

@Composable
fun HomeContent(
    onDrinkWaterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF4FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello User,",
                        fontSize = 18.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Stay Hydrated",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                }

                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF4A90E2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Water Progress Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF5FAFF),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Today's Progress",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                LinearProgressIndicator(
                    progress = { 0.7f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = Color(0xFF4A90E2),
                    trackColor = Color(0xFFD6E9FF)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "1.4L / 2L completed",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    title = "Cups",
                    value = "7",
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    title = "Goal",
                    value = "2L",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            ReminderSelector()

            Spacer(modifier = Modifier.height(24.dp))

            // Reminder Section
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .background(
                         color = Color(0xFFF5FAFF),
                         shape = RoundedCornerShape(24.dp)
                     )
                     .padding(20.dp),
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.SpaceBetween
             ) {
                 Row(
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Icon(
                         imageVector = Icons.Default.Notifications,
                         contentDescription = null,
                         tint = Color(0xFF4A90E2),
                         modifier = Modifier.size(30.dp)
                     )
                     Spacer(modifier = Modifier.width(12.dp))
                     Column {
                         Text(
                             text = "Next Reminder",
                             fontWeight = FontWeight.Bold,
                             fontSize = 18.sp
                         )
                         Text(
                             text = "In 30 minutes",
                             color = Color.Gray
                         )
                     }
                 }
                 Switch(
                     checked = true,
                     onCheckedChange = {}
                 )
             }
            Spacer(modifier = Modifier.height(30.dp))

            // Drink Water Button
            Button(
                onClick = onDrinkWaterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Drink Water",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFF5FAFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A90E2)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ReminderSelector() {
    val reminderOptions = listOf(5, 10, 15, 30, 60)
    var expanded by remember { mutableStateOf(false) }
    var selectedMinute by remember { mutableStateOf(15) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5FAFF),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "Water Reminder",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Every $selectedMinute minutes",
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            reminderOptions.forEach { minute ->
                DropdownMenuItem(
                    text = {
                        Text("$minute minutes")
                    },
                    onClick = {
                        selectedMinute = minute
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeContent()
}

package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun WaterHistoryScreen(
    viewModel: HealthViewModel
) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val history = viewModel.history

    val todayTotal = viewModel.getTodayTotal()
    val weeklyTotal = viewModel.getWeeklyTotal()
    val monthlyTotal = viewModel.getMonthlyTotal()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FC))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 30.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Water History 💧",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Track your hydration activity",
                fontSize = 15.sp,
                color = Color(0xFF9CA3AF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = {
                        Text(
                            "Today",
                            color = if (selectedTab == 0)
                                Color.White
                            else
                                Color(0xFF6B7280)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1565C0),
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp)
                )

                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = {
                        Text(
                            "Weekly",
                            color = if (selectedTab == 1)
                                Color.White
                            else
                                Color(0xFF6B7280)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1565C0),
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp)
                )

                FilterChip(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = {
                        Text(
                            "Monthly",
                            color = if (selectedTab == 2)
                                Color.White
                            else
                                Color(0xFF6B7280)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1565C0),
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBFDDF0)
                ),
                shape = RoundedCornerShape(80.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(32.dp)
                ) {

                    Text(
                        text = when (selectedTab) {
                            0 -> "Today's Intake"
                            1 -> "Weekly Intake"
                            else -> "Monthly Intake"
                        },
                        color = Color(0xFF6B7280),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (selectedTab) {
                            0 -> "${todayTotal}ml"
                            1 -> "${weeklyTotal}ml"
                            else -> "${monthlyTotal}ml"
                        },
                        color = Color(0xFF1565C0),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Recent Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(history.reversed()) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(100.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 16.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        Color(0xFFE3F2FD),
                                        RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.LocalDrink,
                                    contentDescription = null,
                                    tint = Color(0xFF1565C0)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = item.date,
                                    color = Color(0xFF374151),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Water intake recorded",
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                text = "${item.amount}ml",
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
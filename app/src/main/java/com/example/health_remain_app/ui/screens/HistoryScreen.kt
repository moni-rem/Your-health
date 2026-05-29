package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_remain_app.viewmodel.HealthViewModel

@Composable
fun WaterHistoryScreen(
    viewModel: HealthViewModel
) {

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    val weeklyAverage =
        viewModel.getAverageWeekly()

    val monthlyAverage =
        viewModel.getAverageMonthly()

    val goalPercent =
        viewModel.getGoalCompletionPercent()

    val values =
        viewModel.getLast7DaysPercentages()

    val days =
        listOf(
            "Mo",
            "Tu",
            "We",
            "Th",
            "Fr",
            "Sa",
            "Su"
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF5F7FF)
            )
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(20.dp)
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Statistics",
                    color = Color(0xFF4F46E5),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // =========================
            // CHART CARD
            // =========================

            item {

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

                        Text(
                            text = "Weekly Progress",
                            color = Color(0xFF1F2937),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Spacer(
                            modifier = Modifier.height(30.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            horizontalArrangement =
                                Arrangement.SpaceEvenly,
                            verticalAlignment =
                                Alignment.Bottom
                        ) {

                            if (values.isEmpty()) {

                                Text(
                                    text = "No hydration data yet",
                                    color = Color.Gray
                                )

                            } else {

                                values.forEach { value ->

                                    Column(
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .width(24.dp)
                                                .height(
                                                    value.dp
                                                )
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 12.dp,
                                                        topEnd = 12.dp
                                                    )
                                                )
                                                .background(
                                                    brush =
                                                        Brush.verticalGradient(
                                                            listOf(
                                                                blue1,
                                                                blue2
                                                            )
                                                        )
                                                )
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(8.dp)
                                        )

                                        Text(
                                            text =
                                                "${value}%",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================
            // DAILY GOALS
            // =========================

            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {

                        Text(
                            text = "Daily Goals",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            days.forEachIndexed { index, day ->

                                Column(
                                    horizontalAlignment =
                                        Alignment.CenterHorizontally
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(
                                                CircleShape
                                            )
                                            .background(

                                                if (index < 5)
                                                    blue2
                                                else
                                                    Color(
                                                        0xFFE5E7EB
                                                    )
                                            ),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.LocalDrink,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier =
                                                Modifier.size(
                                                    20.dp
                                                )
                                        )
                                    }

                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = day,
                                        fontSize = 13.sp,
                                        color =
                                            if (index < 5)
                                                blue2
                                            else
                                                Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =========================
            // ANALYTICS CARDS
            // =========================

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    AnalyticsCard(
                        title = "Avg/Week",
                        value =
                            "${weeklyAverage}ml/day",
                        modifier =
                            Modifier.weight(1f)
                    )

                    AnalyticsCard(
                        title = "Avg/Month",
                        value =
                            "${monthlyAverage}ml/day",
                        modifier =
                            Modifier.weight(1f)
                    )

                    AnalyticsCard(
                        title = "Goals Done",
                        value =
                            "${goalPercent}%",
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            // =========================
            // RECENT ACTIVITY
            // =========================

            item {

                Text(
                    text = "Recent Activity",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }

            items(
                viewModel.history.reversed()
            ) { item ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Color(0xFFE0F2FE)
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocalDrink,
                                contentDescription = null,
                                tint = blue2
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.date,
                                fontWeight =
                                    FontWeight.Bold,
                                fontSize = 16.sp,
                                color =
                                    Color(0xFF1F2937)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text = item.time,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }

                        Text(
                            text =
                                "${item.amount}ml",
                            color = blue2,
                            fontWeight =
                                FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(100.dp)
                )
            }
        }
    }
}

@Composable
fun AnalyticsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush =
                        Brush.verticalGradient(
                            listOf(
                                blue1.copy(alpha = 0.15f),
                                blue2.copy(alpha = 0.25f)
                            )
                        )
                )
                .padding(20.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                color = Color(0xFF1F2937),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = value,
                color = blue2,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}
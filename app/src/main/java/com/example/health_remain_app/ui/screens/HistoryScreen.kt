package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_remain_app.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterHistoryScreen(
    viewModel: HealthViewModel
) {
    val bluePrimary = Color(0xFF005BEA)
    val lightBlue = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val barBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val textColor = MaterialTheme.colorScheme.onBackground
    val subTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    var selectedRange by remember { mutableIntStateOf(0) } // 0 for Week, 1 for Month
    val ranges = listOf("Week", "Month")

    val weeklyValues = viewModel.getLast7DaysPercentages()
    val monthlyValues = viewModel.getLast30DaysPercentages()
    
    val currentValues = if (selectedRange == 0) weeklyValues else monthlyValues
    val totalAmount = if (selectedRange == 0) viewModel.getWeeklyTotal() else viewModel.getMonthlyTotal()
    val totalLiters = totalAmount.toFloat() / 1000f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "History",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            // Range Selector (Week/Month)
            item {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ranges.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = ranges.size),
                            onClick = { selectedRange = index },
                            selected = index == selectedRange,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = bluePrimary,
                                activeContentColor = Color.White
                            )
                        ) {
                            Text(label)
                        }
                    }
                }
            }

            // --- CHART CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(lightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = bluePrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%.2f L", totalLiters),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = bluePrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Bar Chart Row
                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .horizontalScroll(scrollState),
                            horizontalArrangement = if (selectedRange == 0) Arrangement.SpaceBetween else Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            currentValues.forEachIndexed { index, progress ->
                                val amountLiters = (progress.toFloat() / 100f) * (viewModel.waterGoal / 1000f)
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = if (selectedRange == 0) Modifier.weight(1f) else Modifier.width(36.dp)
                                ) {
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.1f", amountLiters),
                                        fontSize = 10.sp,
                                        color = if (progress >= 80) bluePrimary else subTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(if (selectedRange == 0) 34.dp else 24.dp)
                                            .height(140.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(barBackground)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(progress.toFloat() / 100f)
                                                .align(Alignment.BottomCenter)
                                                .background(if (progress >= 80) bluePrimary else bluePrimary.copy(alpha = 0.4f))
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    val label = if (selectedRange == 0) {
                                        listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").getOrNull(index) ?: ""
                                    } else {
                                        (index + 1).toString()
                                    }
                                    
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = subTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- TODAY'S RECORD SECTION ---
            item {
                Text(
                    text = "Today's record",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        val todayStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        val historyList = viewModel.history.filter { it.date == todayStr }.reversed()

                        // 1. Next Reminder Item (Mock)
                        TimelineItem(
                            icon = Icons.Default.AccessTime,
                            title = "Next reminder",
                            time = "05:00 pm", 
                            isLast = historyList.isEmpty(),
                            isNext = true,
                            iconColor = textColor,
                            circleBg = barBackground
                        )

                        // 2. Hydration Records
                        historyList.forEachIndexed { index, record ->
                            TimelineItem(
                                icon = Icons.Default.WaterDrop,
                                title = "${record.amount}ml",
                                time = record.time,
                                isLast = index == historyList.size - 1,
                                isNext = false,
                                iconColor = bluePrimary,
                                circleBg = lightBlue
                            )
                        }

                        if (historyList.isEmpty()) {
                            Text(
                                text = "No drinks recorded yet today",
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                textAlign = TextAlign.Center,
                                color = subTextColor,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun TimelineItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    time: String,
    isLast: Boolean,
    isNext: Boolean,
    iconColor: Color,
    circleBg: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(circleBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .weight(1f)
                        .padding(vertical = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = if (isNext) FontWeight.Medium else FontWeight.Bold,
                color = if (isNext) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = time,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
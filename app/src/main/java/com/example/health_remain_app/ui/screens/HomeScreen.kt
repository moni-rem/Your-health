package com.example.health_remain_app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.health_remain_app.viewmodel.HealthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sin

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HealthViewModel
) {
    val user = FirebaseAuth.getInstance().currentUser
    val username = user?.displayName ?: "User"

    val todayWater = viewModel.getTodayTotal()
    val goal = viewModel.waterGoal
    val progress = if (goal > 0) (todayWater.toFloat() / goal.toFloat()).coerceIn(0f, 1.1f) else 0f
    val profile = viewModel.userProfile

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)
    val remaining = (goal - todayWater).coerceAtLeast(0)

    // Pulse animation when close to goal (90% to 99%)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (progress >= 0.9f && progress < 1f) 1.06f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.drinkWater(viewModel.defaultDrinkAmount) },
                containerColor = blue2,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(70.dp)
                    .shadow(12.dp, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Water", modifier = Modifier.size(32.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            // Background Animation: Floating Bubbles
            BackgroundBubbles()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // --- PREMIUM HEADER SECTION ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(440.dp)
                        .background(
                            brush = Brush.verticalGradient(listOf(blue1, blue2)),
                            shape = RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = getGreeting(),
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = username,
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                // Hydration Rank
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Stars, null, tint = Color.Yellow.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = getHydrationRank(profile.currentStreak),
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Streak Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                        .clickable { navController.navigate("profile") }
                                ) {
                                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${profile.currentStreak} Days",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = { navController.navigate("profile") },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        // --- ANIMATED WATER DROP ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)) {
                                WaterDropProgress(
                                    progress = progress.coerceAtMost(1f),
                                    modifier = Modifier.size(200.dp)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.offset(y = 30.dp)
                            ) {
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Daily Progress",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                                
                                AnimatedVisibility(
                                    visible = progress >= 1f,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut()
                                ) {
                                    Text(
                                        text = "GOAL REACHED! 🎉",
                                        color = Color.Yellow,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // --- CONTENT SECTION ---
                Column(modifier = Modifier.padding(24.dp)) {
                    
                    // FUN MOTIVATION BANNER (Smooth transitions)
                    MotivationBanner(progress, remaining)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Achievement Preview
                    if (profile.achievements.isNotEmpty()) {
                        Text(
                            text = "Your Milestones",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            profile.achievements.take(5).forEach { achievement ->
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(Color(0xFFFFD700).copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFB8860B), modifier = Modifier.size(22.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Quick Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatChip(
                            label = "Consumed",
                            value = "${todayWater}ml",
                            icon = Icons.Default.LocalDrink,
                            modifier = Modifier.weight(1f)
                        )
                        StatChip(
                            label = "Goal",
                            value = "${goal}ml",
                            icon = Icons.Default.Flag,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Stay Hydrated",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val percent250 = if (goal > 0) (250f / goal * 100).toInt() else 0
                        val percent500 = if (goal > 0) (500f / goal * 100).toInt() else 0

                        WaterActionCard("250ml", "+$percent250%", blue2, Modifier.weight(1f)) { viewModel.drinkWater(250) }
                        WaterActionCard("500ml", "+$percent500%", blue2, Modifier.weight(1f)) { viewModel.drinkWater(500) }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    ReminderTimeCard(viewModel = viewModel)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // DAILY TIP CARD
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE).copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = blue2)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Did you know? Every sip helps you stay focused and energetic!",
                                color = blue2,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun BackgroundBubbles() {
    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")
    val bubbleData = remember {
        List(10) {
            Triple((0..100).random() / 100f, (15..50).random().dp, (5000..12000).random())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        bubbleData.forEach { (startX, size, duration) ->
            val yPos by infiniteTransition.animateFloat(
                initialValue = 1.1f,
                targetValue = -0.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "yPos"
            )

            Box(
                modifier = Modifier
                    .offset(x = (startX * 400).dp, y = (yPos * 800).dp)
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF00C6FB).copy(alpha = 0.04f))
            )
        }
    }
}

fun getGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good Morning,"
        in 12..16 -> "Good Afternoon,"
        in 17..21 -> "Good Evening,"
        else -> "Hello,"
    }
}

fun getHydrationRank(streak: Int): String {
    return when {
        streak >= 30 -> "Hydration God 🔱"
        streak >= 15 -> "Water Legend 🌊"
        streak >= 7 -> "Hydration Pro 🥤"
        streak >= 3 -> "Steady Sipper 💧"
        else -> "Newcomer 🥤"
    }
}

@Composable
fun MotivationBanner(progress: Float, remaining: Int) {
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    
    val state = remember(progress, surfaceVariant, onSurfaceVariant) {
        when {
            progress >= 1f -> MotivationState(Color(0xFFFFD700), Color(0xFFB8860B), "Hydration Master! 🏆\nYou've crushed your goal today!", Icons.Default.EmojiEvents)
            progress >= 0.9f -> MotivationState(Color(0xFF4CAF50), Color(0xFF2E7D32), "So close! Only ${remaining}ml left! 🌟", Icons.Default.AutoAwesome)
            progress >= 0.7f -> MotivationState(Color(0xFF2196F3), Color(0xFF1565C0), "Almost there! 💪\nYou're doing amazing.", Icons.AutoMirrored.Filled.TrendingUp)
            progress >= 0.4f -> MotivationState(Color(0xFF03A9F4), Color(0xFF0277BD), "Halfway there! Keep sipping! 🥤", Icons.Default.Waves)
            progress > 0f -> MotivationState(Color(0xFFE0F2FE), Color(0xFF0288D1), "Great start! Let's keep it up. 💧", Icons.Default.Opacity)
            else -> MotivationState(surfaceVariant, onSurfaceVariant, "Ready for your first glass? 🥤", Icons.Default.LightMode)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = state.bgColor.copy(alpha = 0.15f)
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() + slideInVertically() togetherWith fadeOut() + slideOutVertically() },
            label = "banner"
        ) { target ->
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(target.icon, null, tint = target.textColor, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = target.message,
                    color = target.textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

data class MotivationState(val bgColor: Color, val textColor: Color, val message: String, val icon: ImageVector)

@Composable
fun WaterDropProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    trackColor: Color = Color.White.copy(alpha = 0.2f)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val dropPath = Path().apply {
            moveTo(width / 2f, 0f)
            cubicTo(width * 0.4f, 0f, 0f, height * 0.4f, 0f, height * 0.7f)
            arcTo(
                rect = Rect(Offset(0f, height * 0.4f), Size(width, height * 0.6f)),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            cubicTo(width, height * 0.4f, width * 0.6f, 0f, width / 2f, 0f)
            close()
        }

        drawPath(path = dropPath, color = trackColor)

        clipPath(path = dropPath) {
            val progressHeight = height * progress
            val wavePath = Path().apply {
                val waveAmplitude = 8.dp.toPx()
                val waveLength = width
                moveTo(-waveLength, height - progressHeight)
                for (x in 0..(width.toInt() + waveLength.toInt()) step 4) {
                    val relativeX = x.toFloat() / waveLength
                    val y = (height - progressHeight) + sin(relativeX * 2 * Math.PI + waveOffset).toFloat() * waveAmplitude
                    lineTo(x.toFloat(), y)
                }
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path = wavePath, color = color)
        }
        
        drawPath(
            path = Path().apply {
                moveTo(width * 0.25f, height * 0.55f)
                quadraticTo(width * 0.2f, height * 0.65f, width * 0.25f, height * 0.75f)
            },
            color = Color.White.copy(alpha = 0.3f),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun StatChip(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF005BEA).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color(0xFF005BEA), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WaterActionCard(amount: String, percent: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.94f else 1f, label = "scale")

    Card(
        onClick = onClick,
        modifier = modifier.height(95.dp).scale(scale),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = amount, fontWeight = FontWeight.Bold, color = color, fontSize = 18.sp)
            Text(text = percent, fontSize = 12.sp, color = color.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun ReminderTimeCard(viewModel: HealthViewModel) {
    val reminderOptions = listOf("1 min", "15 min", "30 min", "1 hour", "2 hours", "3 hours")
    var expanded by remember { mutableStateOf(false) }
    
    val currentInterval = viewModel.reminderInterval
    val selectedReminder = when(currentInterval) {
        1 -> "1 min"
        15 -> "15 min"
        30 -> "30 min"
        60 -> "1 hour"
        120 -> "2 hours"
        180 -> "3 hours"
        else -> "30 min"
    }
    
    val blue2 = Color(0xFF005BEA)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFE0F2FE).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, null, tint = blue2)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Hydration Reminder", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Stay consistent to feel better", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = blue2)
            ) {
                Text(selectedReminder, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.KeyboardArrowDown, null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                reminderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            val minutes = when(option) {
                                "1 min" -> 1
                                "15 min" -> 15
                                "30 min" -> 30
                                "1 hour" -> 60
                                "2 hours" -> 120
                                "3 hours" -> 180
                                else -> 30
                            }
                            viewModel.updateReminderInterval(minutes)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

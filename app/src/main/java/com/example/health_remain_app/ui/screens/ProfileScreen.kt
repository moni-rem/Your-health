package com.example.health_remain_app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.health_remain_app.data.model.UserProfile
import com.example.health_remain_app.viewmodel.HealthViewModel
import com.example.health_remain_app.viewmodel.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun ProfileScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    healthViewModel: HealthViewModel = viewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val profile = healthViewModel.userProfile
    val context = LocalContext.current

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploading = true
            healthViewModel.uploadProfilePicture(it) { success ->
                isUploading = false
                if (success) {
                    Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Upload failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
            // =========================
            // TOP HEADER
            // =========================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush = Brush.verticalGradient(listOf(blue1, blue2)),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Profile", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Profile Picture Container
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { photoPickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile.profilePictureUrl.isNotEmpty()) {
                            AsyncImage(
                                model = profile.profilePictureUrl,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        
                        // Edit Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(blue2, CircleShape)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }

                        if (isUploading) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = profile.name.ifEmpty { "User" }, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = currentUser?.email ?: "", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }
            // =========================
            // STATS ROW (Streaks)
            // =========================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-30).dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Current Streak", fontSize = 12.sp, color = Color.Gray)
                        Text("${profile.currentStreak} Days", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = blue2)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Best Streak", fontSize = 12.sp, color = Color.Gray)
                        Text("${profile.bestStreak} Days", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = blue2)
                    }
                }
            }

            // =========================
            // BODY METRICS SECTION
            // =========================
            Text(
                text = "Body Metrics",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem("Weight", "${profile.weight} kg")
                    MetricItem("Height", "${profile.height} cm")
                    val bmi = healthViewModel.calculateBMI()
                    MetricItem("BMI", String.format(Locale.getDefault(), "%.1f", bmi))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // =========================
            // SETTINGS CARD
            // =========================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Personal Info",
                        subtitle = "Name, Weight, Height, Gender",
                        onClick = { showEditProfileDialog = true }
                    )
                    ProfileItem(
                        icon = Icons.Default.EmojiEvents,
                        title = "Achievements",
                        subtitle = "${profile.achievements.size} Unlocked"
                    )
                    ProfileItem(
                        icon = Icons.Default.WaterDrop,
                        title = "Hydration Goal",
                        subtitle = "${healthViewModel.waterGoal}ml recommended",
                        onClick = { navController.navigate("reminder") }
                    )
                    DarkModeItem(
                        isDarkMode = themeViewModel.isDarkMode,
                        onToggle = { themeViewModel.toggleDarkMode() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login") { popUpTo(0) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2), contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            profile = profile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updatedProfile ->
                healthViewModel.updateUserProfile(updatedProfile)
                showEditProfileDialog = false
            },
            viewModel = healthViewModel
        )
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ProfileItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2FE).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF005BEA), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}

@Composable
fun DarkModeItem(
    isDarkMode: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2FE).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFF005BEA))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Dark Mode",
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Switch(checked = isDarkMode, onCheckedChange = { onToggle() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit,
    viewModel: HealthViewModel
) {
    var name by remember { mutableStateOf(profile.name) }
    var weight by remember { mutableStateOf(profile.weight.toString()) }
    var height by remember { mutableStateOf(profile.height.toString()) }
    var gender by remember { mutableStateOf(profile.gender) }
    var activity by remember { mutableStateOf(profile.activityLevel) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Personal Information", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null) }
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.MonitorWeight, null) }
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.Height, null) }
                )
                
                Text("Gender", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = gender == "Male", onClick = { gender = "Male" }, label = { Text("Male") })
                    FilterChip(selected = gender == "Female", onClick = { gender = "Female" }, label = { Text("Female") })
                }

                Text("Activity Level", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Column {
                    activityLevels.forEach { level ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = activity == level, onClick = { activity = level })
                            Text(level, modifier = Modifier.clickable { activity = level }, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                
                HorizontalDivider()
                
                val currentBmi = try {
                    val h = height.toFloat() / 100f
                    val w = weight.toFloat()
                    w / (h * h)
                } catch (e: Exception) { 0f }
                
                if (currentBmi > 0) {
                    Text("BMI: ${String.format(Locale.getDefault(), "%.1f", currentBmi)}", fontWeight = FontWeight.Bold, color = Color(0xFF005BEA))
                    Text("Status: ${getBmiStatus(currentBmi)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    val recommended = viewModel.calculateRecommendedWater()
                    Text("Recommended Water: ${recommended.toInt()}ml/day", fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(profile.copy(
                    name = name,
                    weight = weight.toFloatOrNull() ?: profile.weight,
                    height = height.toFloatOrNull() ?: profile.height,
                    gender = gender,
                    activityLevel = activity
                ))
            }) { Text("Save & Apply") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

val activityLevels = listOf("Sedentary", "Moderate", "Active")

fun getBmiStatus(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Healthy Weight"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }
}

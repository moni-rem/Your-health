package com.example.health_remain_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.health_remain_app.viewmodel.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel
) {

    val auth = FirebaseAuth.getInstance()

    val currentUser = auth.currentUser

    val username =
        currentUser?.displayName ?: "User"

    val email =
        currentUser?.email ?: "No Email"

    val blue1 = Color(0xFF00C6FB)
    val blue2 = Color(0xFF005BEA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF5FBFF)
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
                    .height(340.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
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

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Profile",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(
                                Color.White.copy(alpha = 0.18f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = email,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 15.sp
                    )
                }
            }

            // =========================
            // SETTINGS CARD
            // =========================

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-35).dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {

                    ProfileItem(
                        icon = Icons.Default.Person,
                        title = "Personal Information"
                    )

                    ProfileItem(
                        icon = Icons.Default.Notifications,
                        title = "Notification Settings"
                    )

                    ProfileItem(
                        icon = Icons.Default.WaterDrop,
                        title = "Daily Water Goal",
                        onClick = {
                            navController.navigate("reminder")
                        }
                    )

                    DarkModeItem(
                        isDarkMode = themeViewModel.isDarkMode,
                        onToggle = {
                            themeViewModel.toggleDarkMode()
                        }
                    )

                    ProfileItem(
                        icon = Icons.Default.Settings,
                        title = "App Settings"
                    )
                }
            }

            // =========================
            // LOGOUT BUTTON
            // =========================

            Button(
                onClick = {

                    auth.signOut()

                    navController.navigate("login") {

                        popUpTo(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(65.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue2
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Color(0xFFE0F2FE)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF005BEA)
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = title,
            color = Color(0xFF1F2937),
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
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
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Color(0xFFE0F2FE)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = null,
                tint = Color(0xFF005BEA)
            )
        }

        Spacer(
            modifier = Modifier.width(18.dp)
        )

        Text(
            text = "Dark Mode",
            modifier = Modifier.weight(1f),
            color = Color(0xFF1F2937),
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )

        Switch(
            checked = isDarkMode,
            onCheckedChange = {
                onToggle()
            }
        )
    }
}
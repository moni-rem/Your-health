package com.example.health_remain_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.health_remain_app.viewmodel.HealthViewModel
import com.example.health_remain_app.ui.screens.*
import com.example.health_remain_app.ui.components.BottomNavigation

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: HealthViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            val bottomBarScreens =
                listOf(
                    "home",
                    "reminder",
                    "history",
                    "profile"
                )

            if (currentRoute in bottomBarScreens) {
                BottomNavigation(navController = navController)
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            NavHost(
                navController = navController,
                startDestination = "welcome",
                modifier = Modifier.fillMaxSize()
            ) {

                composable("welcome") {
                    WelcomeScreen(navController)
                }

                composable("login") {
                    LoginScreen(navController)
                }

                composable("register") {
                    RegisterScreen(navController)
                }

                composable("forgot_password") {
                    ForgotPasswordScreen(navController)
                }

                composable("home") {
                    HomeScreen(navController, viewModel)
                }


                composable("history") {
                    WaterHistoryScreen(viewModel)
                }

                composable("profile") {
                    ProfileScreen(navController)
                }

                composable("reminder") {
                    WaterScreen(navController, viewModel)
                }
            }
        }
    }
}
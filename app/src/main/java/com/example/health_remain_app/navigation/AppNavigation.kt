package com.example.health_remain_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.health_remain_app.viewmodel.HealthViewModel
import com.example.health_remain_app.ui.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: HealthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("home") {
            HomeScreen(navController, viewModel)
        }

        composable("water") {
            WaterScreen(viewModel)
        }

        composable("meal") {
            MealScreen(viewModel)
        }

        composable("history") {
            HistoryScreen(viewModel)
        }
    }
}
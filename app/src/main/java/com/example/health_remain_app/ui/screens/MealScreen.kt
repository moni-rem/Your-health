//package com.example.health_remain_app.ui.screens
//
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.health_remain_app.viewmodel.HealthViewModel
//
//@Composable
//fun MealScreen(viewModel: HealthViewModel) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//
//        Text(
//            text = "Meal Tracker 🍽️",
//            fontSize = 28.sp
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        LazyColumn {
//
//            itemsIndexed(viewModel.meals) { index, meal ->
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Text(
//                        text = meal.name,
//                        modifier = Modifier.weight(1f)
//                    )
//
//                    Checkbox(
//                        checked = meal.completed,
//                        onCheckedChange = {
//                            viewModel.toggleMeal(index)
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
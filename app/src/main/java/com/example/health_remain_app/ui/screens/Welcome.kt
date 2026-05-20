package com.example.health_remain_app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.health_remain_app.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {

    WelcomeScreen(
        navController = rememberNavController()
    )
}
@Composable
fun WelcomeScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEAF4FF),
                        Color.White
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // WATER IMAGE
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // TITLE
            Text(
                text = "Health Reminder",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SUBTITLE
            Text(
                text = "Drink Water. Eat Healthy.\nStay Healthy.",
                fontSize = 18.sp,
                color = Color.Gray,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            // BUTTON
            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5EA9FF)
                )
            ) {

                Text(
                    text = "Get Started",
                    fontSize = 18.sp
                )
            }
        }

        // BOTTOM WAVE EFFECT
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(140.dp)
//                .align(Alignment.BottomCenter)
//                .background(
//                    Color(0xFFFFFFFF)
//                )
//        )
    }
}
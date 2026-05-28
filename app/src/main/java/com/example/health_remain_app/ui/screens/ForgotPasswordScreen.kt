package com.example.health_remain_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember {
        mutableStateOf("")
    }

    val MainTextColor = Color(0xFF1E293B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F9FC),
                        Color(0xFFBFDDF0)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.35f)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Forgot Password",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainTextColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Enter your email to receive a password reset link",
                        fontSize = 15.sp,
                        color = MainTextColor.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text(
                                text = "Email Address",
                                color = MainTextColor
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7DB9DE),
                            unfocusedBorderColor = Color(0xFF7DB9DE),
                            focusedTextColor = MainTextColor,
                            unfocusedTextColor = MainTextColor,
                            cursorColor = MainTextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {

                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {

                                        Toast.makeText(
                                            context,
                                            "Reset email sent",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    } else {

                                        Toast.makeText(
                                            context,
                                            task.exception?.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7DB9DE)
                        )
                    ) {

                        Text(
                            text = "Send Reset Email",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainTextColor
                        )
                    }
                }
            }
        }
    }
}
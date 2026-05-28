package com.example.health_remain_app.ui.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenView() {
    LoginScreen(
        navController = rememberNavController()
    )
}

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    val context = LocalContext.current
    val activity = context as Activity

    val googleSignInClient: GoogleSignInClient =
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken("87580379256-621f4n21le4n488q4srpf6p4ece4k0he.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val task =
            GoogleSignIn.getSignedInAccountFromIntent(result.data)

        if (task.isSuccessful) {

            val account = task.result

            val credential =
                GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )

            auth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->

                    if (authTask.isSuccessful) {

                        val userEmail =
                            auth.currentUser?.email

                        val db =
                            FirebaseFirestore.getInstance()

                        db.collection("users")
                            .whereEqualTo(
                                "email",
                                userEmail
                            )
                            .get()
                            .addOnSuccessListener { documents ->

                                if (!documents.isEmpty) {

                                    Toast.makeText(
                                        context,
                                        "Google Login Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("home")

                                } else {

                                    auth.signOut()

                                    googleSignInClient.signOut()

                                    Toast.makeText(
                                        context,
                                        "This account is not registered",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                    } else {

                        Toast.makeText(
                            context,
                            authTask.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFCDDAEA),
                        Color(0xFF9BB7DA)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Water Reminder",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stay healthy by drinking enough water",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                visualTransformation =
                    PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {

                    if (
                        email.isNotEmpty() &&
                        password.isNotEmpty()
                    ) {

                        auth.signInWithEmailAndPassword(
                            email,
                            password
                        )
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        context,
                                        "Login Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("home")

                                } else {

                                    Toast.makeText(
                                        context,
                                        task.exception?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                    } else {

                        Toast.makeText(
                            context,
                            "Please enter email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                )
            ) {

                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    val signInIntent: Intent =
                        googleSignInClient.signInIntent

                    launcher.launch(signInIntent)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                )
            ) {

                Text(
                    text = "Continue with Google",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {

                }
            ) {

                Text(
                    text = "Forgot Password?",
                    color = Color.Blue,
                    modifier = Modifier.clickable {

                        navController.navigate("forgot_password")

                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Don't have an account?",
                    color = Color.Black
                )

                TextButton(
                    onClick = {
                        navController.navigate("register")
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {

                    Text(
                        text = "Register",
                        color = Color(0xFF4A90E2)
                    )
                }
            }
        }
    }
}

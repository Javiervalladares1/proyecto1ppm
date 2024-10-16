

package com.example.proyecto1ppm.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.compose.rememberNavController

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto1ppmTheme {
                RegistrationScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .background(Color(0xFF6A1B9A))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo and App Name
        Image(
            painter = painterResource(id = R.drawable.gorro_graduacion),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
        Text(
            text = "StudyMatch",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // First Name Field
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Field
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Buttons for Registration and Login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Register User
                if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val uid = user?.uid

                                val userMap = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "email" to email
                                )

                                if (uid != null) {
                                    db.collection("users").document(uid).set(userMap)
                                        .addOnSuccessListener {
                                            println("User data saved successfully")
                                            navController.navigate("user_profile_screen")
                                        }
                                        .addOnFailureListener { exception ->
                                            errorMessage = "Error saving user data: ${exception.message}"
                                        }
                                }
                            } else {
                                errorMessage = "Registration error: ${task.exception?.message}"
                            }
                        }
                } else {
                    errorMessage = "Please fill all the fields"
                }
            }) {
                Text("Register")
            }

            Button(onClick = {
                // Login User
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("Login successful with email: $email")
                                navController.navigate("home_screen")
                            } else {
                                errorMessage = "Login error: ${task.exception?.message}"
                            }
                        }
                } else {
                    errorMessage = "Please fill all the fields"
                }
            }) {
                Text("Login")
            }
        }
    }
}

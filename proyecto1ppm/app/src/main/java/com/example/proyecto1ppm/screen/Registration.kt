package com.example.proyecto1ppm.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.R
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistrationScreen(navController = rememberNavController())
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) } // Mostrar mensaje de error si el login falla
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .background(Color(0xFF6A1B9A))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Agregar la imagen en el centro superior
        Image(
            painter = painterResource(id = R.drawable.gorro_graduacion),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 0.dp)
        )
        Text(
            text = "StudyMatch",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de texto para el email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error (si es necesario)
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Botones para Registro y Login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Registrar el usuario en Firebase Authentication
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("Registro exitoso con email: $email")
                                // Navegar al perfil del usuario
                                navController.navigate("user_profile_screen")
                            } else {
                                errorMessage = "Error en el registro: ${task.exception?.message}"
                            }
                        }
                } else {
                    errorMessage = "Por favor, rellena todos los campos"
                }
            }) {
                Text("Registrarse")
            }

            Button(onClick = {
                // Iniciar sesión con Firebase Authentication
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("Inicio de sesión exitoso con email: $email")
                                // Navegar a la pantalla de inicio (home_screen)
                                navController.navigate("home_screen")
                            } else {
                                errorMessage = "Error en el inicio de sesión: ${task.exception?.message}"
                            }
                        }
                } else {
                    errorMessage = "Por favor, rellena todos los campos"
                }
            }) {
                Text("Iniciar Sesión")
            }
        }
    }
}

package com.example.proyecto1ppm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "registration_screen") {
                composable("registration_screen") {
                    RegistrationScreen(navController = navController)
                }
                composable("login_screen") {
                    LoginScreen(navController = navController)
                }
            }
        }
    }
}
@Composable
fun RegistrationScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .background(Color(0xFF6A1B9A))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Imagen en el centro superior
            Image(
                painter = painterResource(id = R.drawable.gorro_graduacion),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp) // Ajustar tamaño de la imagen
                    .padding(bottom = 8.dp)
            )
        }

        item {
            Text(
                text = "StudyMatch",
                color = Color.White,
                fontSize = 30.sp, // Ajustar el tamaño del texto
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Box(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        }

        item {
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Box(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        }

        item {
            TextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("Areas de interés/estudio") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Box(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        }

        item {
            TextField(
                value = institution,
                onValueChange = { institution = it },
                label = { Text("Universidad/Institución") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Box(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        }

        item {
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre Completo ") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Box(modifier = Modifier.height(16.dp)) // Espacio más grande antes del botón
        }

        item {
            Button(onClick = {
                println("Registrarse: Email: $email, Password: $password")
                navController.navigate("home_screen")
            }) {
                Text("Registrarse")
            }
        }

        item {
            Box(modifier = Modifier.height(16.dp)) // Espacio más grande antes del texto de login
        }

        item {
            TextButton(onClick = {
                navController.navigate("login_screen")
            }) {
                Text(
                    "¿Ya tienes una cuenta? Inicia sesión",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}



@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color(0xFF6A1B9A))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contaseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            println("Iniciar Sesión: Email: $email, Password: $password")
            navController.navigate("home_screen")
        }) {
            Text("Iniciar Sesión")

        }
        Spacer(modifier = Modifier.height(2.dp))
        TextButton(onClick = {
            navController.navigate("registration_screen")
        }) {
            Text("¿No tienes una cuenta? Regístrate", color = Color.White, fontSize = 14.sp)
        }
    }
}

// Preview para la pantalla de registro
@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = rememberNavController())
}

// Preview para la pantalla de inicio de sesión
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}



package com.example.proyecto1ppm.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation


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
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "StudyMatch",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de texto: Nombre, Apellido, Email, Contraseña
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar error si existe
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        // Botón de Registro
        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
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
                    }  else {
                        errorMessage = "Comprueba que tus credenciales sean correctas"
                    }
                }
            } else {
                errorMessage = "Completa todos los campos"
            }
        }) {
            Text("Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¿Ya tienes cuenta? Inicia Sesión",
            color = Color.White,
            modifier = Modifier.clickable {
                navController.navigate("LoginScreen")
            }
        )


    }
}


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()

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
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "StudyMatch",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de texto: Email y Contraseña
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar error si existe
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        // Botón de Inicio de Sesión
        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Lógica de inicio de sesión
                        navController.navigate("home_screen")
                    } else {
                        errorMessage = "Verifica que tus credenciales sean correctas"
                    }
                }
            } else {
                errorMessage = "Completa todos los campos"
            }
        }) {
            Text("Iniciar Sesión")
        }

        // Texto de navegación a pantalla de registro
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = Color.White,
            modifier = Modifier.clickable {
                navController.navigate("registration_screen")
            }
        )
    }
}







package com.example.proyecto1ppm

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistrationScreen()
        }
    }
}


@Composable
fun RegistrationScreen() {
    var email by remember { mutableStateOf("email") }
    var password by remember { mutableStateOf("password") }
    var interests by remember { mutableStateOf("interests") }
    var institution by remember { mutableStateOf("institution") }
    var fullName by remember { mutableStateOf("fullName") }



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
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = interests,
            onValueChange = { interests = it },
            label = { Text("Areas of study/Interests") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = institution,
            onValueChange = { institution = it },
            label = { Text("University/Institution") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Aquí podemos usar Retrofit para llamar a una API y validar datos
                println("Registrarse: Email: $email, Password: $password")
            }) {
                Text("Registrarse")
            }
            Button(onClick = {
                // Aquí igual podemos usar Retrofit para lo mismo
                println("Iniciar Sesión: Email: $email, Password: $password")
            }) {
                Text("Iniciar Sesión")
            }
        }
    }

}

//preview para la pantalla registration
@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}
package com.example.proyecto1ppm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SlideMenuScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto1ppmTheme() {
                SlideMenuScreen()
            }
        }
    }
}
@Composable
fun SlideMenuScreen() {
    Row(modifier = Modifier.fillMaxSize()) {
        // Menú deslizante (morado)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .background(Color(0xFF673AB7)) // Color morado
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Opciones del menú
                Column {
                    MenuItem(text = "CURSOS", onClick = { /* Acción */ })
                    MenuItem(text = "GRUPOS", onClick = { /* Acción */ })
                    MenuItem(text = "SUGERENCIAS", onClick = { /* Acción */ })
                    MenuItem(text = "AJUSTES", onClick = { /* Acción */ })
                    MenuItem(text = "CERRAR SESIÓN", onClick = { /* Acción */ })
                }

                // Imagen del usuario y nombre
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Acción de click en perfil */ }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gorro_graduacion), // Imagen del usuario
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HARRY TRUMAN",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Franja blanca
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.White)
        )
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun SlideMenuScreenPreview() {
    Proyecto1ppmTheme() {
        SlideMenuScreen()
    }
}

package com.example.proyecto1ppm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.R

@Composable
fun GroupDetailsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado con botón de regresar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.navigate("home_screen") }
            )
            Text(
                text = "Curso de Ejemplo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de portada del grupo
        Image(
            painter = painterResource(R.drawable.house), // Imagen genérica
            contentDescription = "Imagen del Curso",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del grupo
        Text(
            text = "Descripción",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Esta es una descripción genérica para el curso de ejemplo.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Temas Abordados
        Text(
            text = "Temas Abordados",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column {
            listOf("Tema 1", "Tema 2", "Tema 3").forEach { topic ->
                Text(text = "- $topic", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apartado de reuniones de Zoom
        Text(
            text = "Reuniones",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Reuniones por Zoom: Miércoles a las 5:00 PM",
            fontSize = 16.sp
        )
        Text(
            text = "Enlace de la reunión: https://zoom.us/j/1234567890",
            fontSize = 16.sp,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Primera fila de botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Enviar mensaje */ }) {
                Text("Enviar Mensaje")
            }
            Button(onClick = { /* Agregar miembro */ }) {
                Text("Agregar Miembro")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Segunda fila: Botón de "Salir del Grupo" en una fila aparte
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centramos el botón rojo
        ) {
            Button(
                onClick = { /* Salir del grupo */ },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Salir del Grupo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensajes recientes o publicaciones
        Text(
            text = "Mensajes Recientes",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Placeholder para mensajes recientes
        Column {
            repeat(3) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Usuario $index", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Este es un mensaje reciente en el grupo.")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupDetailsScreen() {
    GroupDetailsScreen(navController = rememberNavController() )
}

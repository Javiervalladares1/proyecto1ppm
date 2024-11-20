package com.example.proyecto1ppm.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.data.Course
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CourseDetailScreen(navController: NavController, courseId: String) {
    val db = FirebaseFirestore.getInstance()
    var course by remember { mutableStateOf<Course?>(null) }

    // Obtener el curso desde Firestore
    LaunchedEffect(courseId) {
        db.collection("courses").document(courseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    course = document.toObject(Course::class.java)
                } else {
                    println("El documento no existe")
                }
            }
            .addOnFailureListener { exception ->
                println("Error al obtener el documento: ${exception.message}")
            }
    }

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
                contentDescription = "Atrás",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text(
                text = course?.title ?: "Curso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del curso usando AsyncImage de Coil
        AsyncImage(
            model = course?.imageUrl ?: "",
            contentDescription = "Imagen del Curso",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del curso
        Text(
            text = "Descripción",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = course?.description ?: "Descripción no disponible.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Reuniones
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

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    // Navegar al Chat
                    val encodedCourseId = URLEncoder.encode(courseId, StandardCharsets.UTF_8.toString())
                    navController.navigate("chat_screen/$encodedCourseId")
                }
            ) {
                Text("Chat")
            }
            Button(
                onClick = {
                    // Salir del grupo
                    // Lógica para salir del grupo
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Salir del Grupo")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

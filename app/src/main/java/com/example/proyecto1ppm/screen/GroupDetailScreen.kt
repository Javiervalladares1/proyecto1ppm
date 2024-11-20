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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GroupDetailsScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var userGroups by remember { mutableStateOf<List<String>>(emptyList()) }
    var coursesInGroups by remember { mutableStateOf<List<Course>>(emptyList()) }

    LaunchedEffect(userId) {
        if (userId != null) {
            // Obtener grupos donde el usuario es miembro
            db.collection("groups")
                .whereArrayContains("members", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val groupIds = querySnapshot.documents.map { it.id }
                    userGroups = groupIds

                    // Obtener información de los cursos correspondientes desde Firestore
                    if (groupIds.isNotEmpty()) {
                        db.collection("courses")
                            .whereIn("id", groupIds)
                            .get()
                            .addOnSuccessListener { coursesSnapshot ->
                                val courses = coursesSnapshot.documents.mapNotNull { it.toObject(Course::class.java) }
                                coursesInGroups = courses
                            }
                            .addOnFailureListener { e ->
                                println("Error al obtener los cursos: ${e.message}")
                            }
                    } else {
                        coursesInGroups = emptyList()
                    }
                }
                .addOnFailureListener { e ->
                    println("Error al obtener los grupos del usuario: ${e.message}")
                }
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
                modifier = Modifier.clickable { navController.navigate("home_screen") }
            )
            Text(
                text = "Mis Grupos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (coursesInGroups.isNotEmpty()) {
            coursesInGroups.forEach { course ->
                // Imagen del curso usando AsyncImage de Coil
                AsyncImage(
                    model = course.imageUrl,
                    contentDescription = "Imagen del Curso",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción del curso
                Text(
                    text = course.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = course.description,
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
                            val encodedCourseId = URLEncoder.encode(
                                course.id,
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("chat_screen/$encodedCourseId")
                        }
                    ) {
                        Text("Chat")
                    }
                    Button(
                        onClick = {
                            // Salir del grupo
                            if (userId != null) {
                                db.collection("groups").document(course.id)
                                    .update("members", FieldValue.arrayRemove(userId))
                                    .addOnSuccessListener {
                                        // Usuario removido del grupo
                                        // Actualizar listas locales
                                        userGroups = userGroups - course.id
                                        coursesInGroups = coursesInGroups.filter { it.id != course.id }
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Salir del Grupo")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            Text(
                text = "No estás en ningún grupo.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

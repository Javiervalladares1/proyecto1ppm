package com.example.proyecto1ppm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.R
import com.example.proyecto1ppm.data.Course
import com.example.proyecto1ppm.data.CourseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.*

import androidx.compose.foundation.lazy.items

import androidx.compose.ui.*
import com.google.firebase.firestore.FieldValue
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val courses = CourseRepository.courses
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        TopAppBar(
            title = {
                Text("SUGERENCIA DE CURSOS", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("slide_menu_screen") }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = { /* Search action */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }
            }
        )

        // Tabs
        TabRow(selectedTabIndex = 0, modifier = Modifier.fillMaxWidth()) {
            Tab(
                selected = true,
                onClick = { /* Switch to Cursos */ },
                text = { Text("Cursos") }
            )
            Tab(
                selected = false,
                onClick = {
                    navController.navigate("group_detail_screen")
                },
                text = { Text("Grupos") }
            )
            Tab(
                selected = false,
                onClick = { /* Switch to Sugerencias */ },
                text = { Text("Sugerencias") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Carrusel de Cursos
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                Image(
                    painter = painterResource(course.imageRes),
                    contentDescription = course.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            selectedCourse = course
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Información del Curso seleccionado
        if (selectedCourse != null) {
            Text(
                text = selectedCourse!!.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones "Join Group" y "Ver Grupo"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    // Acción para unirse al grupo
                    val userId = auth.currentUser?.uid
                    val groupId = selectedCourse!!.id
                    if (userId != null) {
                        db.collection("groups").document(groupId)
                            .update("members", FieldValue.arrayUnion(userId))
                            .addOnSuccessListener {
                                // Usuario añadido al grupo
                            }
                            .addOnFailureListener { e ->
                                // Si el grupo no existe, lo creamos
                                val groupData = hashMapOf(
                                    "courseId" to groupId,
                                    "members" to listOf(userId)
                                )
                                db.collection("groups").document(groupId)
                                    .set(groupData)
                                    .addOnSuccessListener {
                                        // Grupo creado y usuario añadido
                                    }
                            }
                    }
                }) {
                    Text("Unirse al Grupo")
                }

                Button(onClick = {
                    // Navegar a la nueva pantalla de miembros del grupo
                    val encodedCourseId = URLEncoder.encode(selectedCourse!!.id, StandardCharsets.UTF_8.toString())
                    navController.navigate("group_members_screen/$encodedCourseId")
                }) {
                    Text("Ver Grupo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción del Curso
            Text(
                text = selectedCourse!!.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            // Mostrar un mensaje o un curso por defecto
            Text(
                text = "Seleccione un curso para ver más detalles.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen( navController = rememberNavController())
}

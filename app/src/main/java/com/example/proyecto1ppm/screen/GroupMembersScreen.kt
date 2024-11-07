package com.example.proyecto1ppm.screen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.data.CourseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import com.google.firebase.firestore.FieldValue
import androidx.compose.runtime.mutableStateMapOf


@Composable
fun GroupMembersScreen(navController: NavController, courseId: String) {
    println("GroupMembersScreen: courseId = $courseId")

    if (courseId.isEmpty()) {
        println("Error: courseId está vacío")
        // Mostrar mensaje al usuario o navegar atrás
        return
    }

    val course = CourseRepository.courses.find { it.id == courseId }
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var members by remember { mutableStateOf<List<String>>(emptyList()) }
    val userNames = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(courseId) {
        try {
            db.collection("groups").document(courseId).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al obtener los datos del grupo: ${error.message}")
                } else if (snapshot != null && snapshot.exists()) {
                    val groupData = snapshot.data
                    val membersList = groupData?.get("members") as? List<*>
                    val memberIds = membersList?.mapNotNull { it as? String } ?: emptyList()
                    members = memberIds

                    // Obtener los nombres de los miembros
                    memberIds.forEach { memberId ->
                        if (!userNames.containsKey(memberId)) {
                            db.collection("users").document(memberId).get()
                                .addOnSuccessListener { document ->
                                    if (document != null && document.exists()) {
                                        val firstName = document.getString("firstName") ?: ""
                                        val lastName = document.getString("lastName") ?: ""
                                        val fullName = "$firstName $lastName"
                                        userNames[memberId] = fullName
                                    } else {
                                        userNames[memberId] = "Usuario desconocido"
                                    }
                                }
                                .addOnFailureListener {
                                    userNames[memberId] = "Error al obtener nombre"
                                }
                        }
                    }
                } else {
                    println("El documento del grupo no existe.")
                }
            }
        } catch (e: Exception) {
            println("Excepción al obtener los miembros del grupo: ${e.message}")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado con botón de regresar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = course?.title ?: "Grupo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Miembros del Grupo:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Lista de miembros
        if (members.isNotEmpty()) {
            items(members) { memberId ->
                val name = userNames[memberId] ?: "Cargando..."
                Text(text = name, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            item {
                Text(text = "No hay miembros en este grupo.", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Botón para salir del grupo
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (userId != null) {
                        db.collection("groups").document(courseId)
                            .update("members", FieldValue.arrayRemove(userId))
                            .addOnSuccessListener {
                                // Usuario removido del grupo
                                navController.popBackStack()
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salir del Grupo")
            }
        }
    }
}


package com.example.proyecto1ppm.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
// Eliminamos importación de painterResource
// import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.data.Course
import com.example.proyecto1ppm.data.Suggestion
import com.example.proyecto1ppm.data.SuggestionRepository
import com.example.proyecto1ppm.data.SuggestionType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Importamos Coil
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableStateOf(0) }

    // Cargar cursos desde Firestore
    LaunchedEffect(Unit) {
        db.collection("courses")
            .get()
            .addOnSuccessListener { result ->
                val loadedCourses = result.map { document ->
                    document.toObject(Course::class.java)
                }
                courses = loadedCourses
            }
            .addOnFailureListener { exception ->
                println("Error al obtener cursos: ${exception.message}")
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "STUDYMATCH",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("slide_menu_screen") }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs
            TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Cursos") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Grupos") }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("Sugerencias") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTabIndex) {
                0 -> {
                    // Mostrar cursos en LazyRow
                    CourseCarousel(
                        courses = courses,
                        selectedCourse = selectedCourse,
                        onCourseSelected = { course -> selectedCourse = course },
                        navController = navController,
                        auth = auth,
                        db = db,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = coroutineScope
                    )
                }
                1 -> {
                    // Mostrar grupos de manera más estética
                    GroupList(navController = navController)
                }
                2 -> {
                    // Mostrar sugerencias
                    SuggestionList()
                }
            }
        }
    }
}

@Composable
fun CourseCarousel(
    courses: List<Course>,
    selectedCourse: Course?,
    onCourseSelected: (Course) -> Unit,
    navController: NavController,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    // Carrusel de Cursos
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses.size) { index ->
            val course = courses[index]
            AsyncImage(
                model = course.imageUrl,
                contentDescription = course.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        onCourseSelected(course)
                    }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Información del Curso seleccionado
    if (selectedCourse != null) {
        Text(
            text = selectedCourse.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botones "Unirse al Grupo" y "Ver Grupo"
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
                val groupId = selectedCourse.id
                if (userId != null) {
                    db.collection("groups").document(groupId)
                        .update("members", FieldValue.arrayUnion(userId))
                        .addOnSuccessListener {
                            // Usuario añadido al grupo
                            // Mostrar mensaje de felicitación y navegar
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "¡Felicitaciones! Te has unido al grupo de ${selectedCourse.title}.",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                                    // Navegar a la pantalla principal del curso
                                    val encodedCourseId = URLEncoder.encode(selectedCourse.id, StandardCharsets.UTF_8.toString())
                                    navController.navigate("course_detail_screen/$encodedCourseId")
                                }
                            }
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
                                    coroutineScope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "¡Felicitaciones! Te has unido al grupo de ${selectedCourse.title}.",
                                            actionLabel = "OK",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                                            // Navegar a la pantalla principal del curso
                                            val encodedCourseId = URLEncoder.encode(selectedCourse.id, StandardCharsets.UTF_8.toString())
                                            navController.navigate("course_detail_screen/$encodedCourseId")
                                        }
                                    }
                                }
                        }
                }
            }) {
                Text("Unirse al Grupo")
            }

            Button(onClick = {
                // Navegar a la nueva pantalla de miembros del grupo
                val encodedCourseId = URLEncoder.encode(
                    selectedCourse.id,
                    StandardCharsets.UTF_8.toString()
                )
                navController.navigate("group_members_screen/$encodedCourseId")
            }) {
                Text("Ver Grupo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del Curso
        Text(
            text = selectedCourse.description,
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

@Composable
fun GroupList(navController: NavController) {
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

    if (coursesInGroups.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(coursesInGroups) { course ->
                GroupItem(course = course, navController = navController, userId = userId)
            }
        }
    } else {
        Text(
            text = "No estás en ningún grupo.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GroupItem(course: Course, navController: NavController, userId: String?) {
    val db = FirebaseFirestore.getInstance()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Navegar a los detalles del curso
                val encodedCourseId = URLEncoder.encode(course.id, StandardCharsets.UTF_8.toString())
                navController.navigate("course_detail_screen/$encodedCourseId")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Imagen del curso
            AsyncImage(
                model = course.imageUrl,
                contentDescription = "Imagen del Curso",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título y descripción
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = course.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = course.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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
                                    // Opcional: Actualizar la lista localmente
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Salir del Grupo")
                }
            }
        }
    }
}

@Composable
fun SuggestionList() {
    val suggestions = SuggestionRepository.suggestions

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sugerencias para estudiar solo
        val soloSuggestions = suggestions.filter { it.type == SuggestionType.SOLO }

        item {
            Text(
                text = "Sugerencias para Estudiar Solo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(soloSuggestions) { suggestion ->
            SuggestionItem(suggestion = suggestion)
        }

        // Sugerencias para estudiar en grupo
        val groupSuggestions = suggestions.filter { it.type == SuggestionType.GROUP }

        item {
            Text(
                text = "Sugerencias para Estudiar en Grupo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(groupSuggestions) { suggestion ->
            SuggestionItem(suggestion = suggestion)
        }
    }
}

@Composable
fun SuggestionItem(suggestion: Suggestion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = suggestion.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = suggestion.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

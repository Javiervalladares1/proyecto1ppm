

package com.example.proyecto1ppm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.R
import com.google.firebase.auth.FirebaseAuth
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto1ppm.utils.getUserFullName
import androidx.compose.runtime.LaunchedEffect

@Composable
fun UserProfileScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var fullName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        getUserFullName { name ->
            fullName = name
            if (name.isNotEmpty()) {
                // Construct the avatar URL using the API
                avatarUrl = "https://avatar.iran.liara.run/username?username=${name.replace(" ", "")}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Menu Icon
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.clickable { navController.navigate("slide_menu_screen") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Image
        if (avatarUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            // Placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Personal Details
        Text(
            text = "Detalles Personales",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        UserDetailField(label = "Nombre Completo", value = fullName)
        UserDetailField(label = "Email", value = currentUser?.email ?: "")
        ChangeOptionText("Cambiar Email")

        UserDetailField(label = "Contraseña", value = "************", isPassword = true)
        ChangeOptionText("Cambiar Contraseña")

        Spacer(modifier = Modifier.height(16.dp))

        // Assigned Courses
        Text(
            text = "Cursos Asignados",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        AssignedCourses(courses = listOf("Cálculo Diferencial", "Química Fundamental", "Física Universitaria"))
    }
}

@Composable
fun UserDetailField(label: String, value: String, isPassword: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        BasicTextField(
            value = value,
            onValueChange = {},
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(12.dp)
        )
    }
}

@Composable
fun ChangeOptionText(option: String) {
    Text(
        text = option,
        color = Color(0xFF673AB7),
        modifier = Modifier
            .clickable { /* Acción para cambiar */ }
            .padding(8.dp)
    )
}

@Composable
fun AssignedCourses(courses: List<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        courses.forEach { course ->
            Text(text = course, modifier = Modifier.padding(4.dp))
        }
    }
}

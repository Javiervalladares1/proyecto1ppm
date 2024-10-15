package com.example.proyecto1ppm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserProfileScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Icono de menú hamburguesa
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.clickable { navController.navigate("slide_menu_screen") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título "Perfil"
        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de perfil
        Image(
            painter = painterResource(id = R.drawable.house),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Detalles del perfil
        Text(text = "Detalles Personales", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        UserDetailField(label = "Email", value = currentUser?.email ?: "", isPassword = false)
        ChangeOptionText("Change Email")

        UserDetailField(label = "Password", value = "************", isPassword = true)
        ChangeOptionText("Change Password")

        Spacer(modifier = Modifier.height(16.dp))

        // Cursos asignados
        Text(text = "Cursos asignados", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        AssignedCourses(courses = listOf("Cálculo diferencial", "Química Fundamental", "Física Universitaria"))
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

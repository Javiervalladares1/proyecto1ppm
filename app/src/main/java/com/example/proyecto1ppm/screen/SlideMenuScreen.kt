

package com.example.proyecto1ppm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto1ppm.R
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto1ppm.utils.getUserFullName
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource

@Composable
fun SlideMenuScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        getUserFullName { name ->
            fullName = name
            if (name.isNotEmpty()) {
                avatarUrl = "https://avatar.iran.liara.run/username?username=${name.replace(" ", "")}"
            }
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Slide Menu
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .background(Color(0xFF673AB7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Options
                Column {
                    MenuItem(text = "CURSOS", onClick = {
                        navController.navigate("home_screen")
                    })
                    MenuItem(text = "GRUPOS", onClick = {
                        navController.navigate("group_detail_screen")
                    })
                    MenuItem(text = "SUGERENCIAS", onClick = { /* Acción */ })
                    MenuItem(text = "AJUSTES", onClick = {
                        navController.navigate("user_profile_screen")
                    })
                    MenuItem(text = "CERRAR SESIÓN", onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("registration_screen") {
                            popUpTo("registration_screen") { inclusive = true }
                        }
                    })
                }

                // User Profile Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("user_profile_screen")
                    }
                ) {
                    if (avatarUrl.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(avatarUrl),
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        // Placeholder image
                        Image(
                            painter = painterResource(id = R.drawable.house),
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fullName.uppercase(),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // White Stripe
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onClick() }
    )
}

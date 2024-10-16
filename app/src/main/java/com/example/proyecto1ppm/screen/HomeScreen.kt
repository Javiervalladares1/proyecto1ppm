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

data class Group(
    val title: String,
    val description: String,
    val image: Int,
    val members: List<Int>, // Image resource ids for member avatars
    val topics: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        TopAppBar(
            title = {
                Text("SUGERENCIA DE CURSOS", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            },
            navigationIcon = {
                IconButton(onClick = {  navController.navigate("slide_menu_screen") }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = { /* Search action */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }
            }
        )

        // Tabs: Cursos, Grupos, Sugerencias
        TabRow(selectedTabIndex = 0, modifier = Modifier.fillMaxWidth()) {
            Tab(
                selected = true,
                onClick = { /* Switch to Cursos */ },
                text = { Text("Cursos") }
            )
            Tab(
                selected = false,
                onClick = {
                    navController.navigate("group_detail_screen") },
                text = { Text("Grupos") }
            )
            Tab(
                selected = false,
                onClick = { /* Switch to Sugerencias */ },
                text = { Text("Sugerencias") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Carrusel de Cursos (sin datos, solo placeholder)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(3) { // Simulamos 3 items de ejemplo
                Image(
                    painter = painterResource(R.drawable.house), // Imagen genérica
                    contentDescription = "Curso",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Información del Curso (placeholder)
        Text(
            text = "Cálculo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sección de Avatares y Botones "Join Group" y "Create New Group" (sin lógica)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyRow {
                    items(3) { // Simulamos 3 avatares de ejemplo
                        Image(
                            painter = painterResource(R.drawable.house),
                            contentDescription = "Member Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .padding(4.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(painterResource(R.drawable.house), contentDescription = "Join Group", modifier = Modifier.size(24.dp))
                    Text("Join group", color = Color.Blue, fontSize = 14.sp)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painterResource(R.drawable.house), contentDescription = "Create New Group", modifier = Modifier.size(24.dp))
                Text("Create new group", color = Color.Blue, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del Curso (placeholder)
        Text(
            text = "Esta es una descripción del curso de ejemplo.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen( navController = rememberNavController())
}

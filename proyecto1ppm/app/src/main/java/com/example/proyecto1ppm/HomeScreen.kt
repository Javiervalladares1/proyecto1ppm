package com.example.proyecto1ppm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

data class Group(
    val title: String,
    val description: String,
    val image: Int,
    val members: List<Int>, // Image resource ids for member avatars
    val topics: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(groups: List<Group>) {
    var selectedGroup by remember { mutableStateOf(groups[0]) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        TopAppBar(
            title = {
                Text("SUGERENCIA DE CURSOS", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            },
            navigationIcon = {
                IconButton(onClick = { /* Open menu */ }) {
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
                onClick = { /* Switch to Grupos */ },
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
            items(groups) { group ->
                Image(
                    painter = painterResource(group.image),
                    contentDescription = group.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Información del Curso Seleccionado
        Text(
            text = selectedGroup.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sección de Avatares y Botones "Join Group" y "Create New Group"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround, // Espaciado uniforme
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna de Avatares y "Join Group"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatares de Miembros
                LazyRow {
                    items(selectedGroup.members) { memberImage ->
                        Image(
                            painter = painterResource(memberImage),
                            contentDescription = "Member Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .padding(4.dp)
                        )
                    }
                }

                // "Join Group" Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(painterResource(R.drawable.house), contentDescription = "Join Group", modifier = Modifier.size(24.dp))
                    Text("Join group", color = Color.Blue, fontSize = 14.sp)
                }
            }

            // Columna de "Create New Group"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(R.drawable.house), contentDescription = "Create New Group", modifier = Modifier.size(24.dp))
                Text("Create new group", color = Color.Blue, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del Curso
        Text(
            text = selectedGroup.description,
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
    val groups = listOf(
        Group(
            title = "Cálculo Diferencial",
            description = "Aquí va una breve descripción del curso",
            image = R.drawable.house,
            members = listOf(R.drawable.house, R.drawable.house, R.drawable.house),
            topics = listOf("Límites", "Derivadas", "Integrales", "Aplicaciones de Derivadas")
        ),
        Group(
            title = "Química General",
            description = "Aquí va una breve descripción del curso",
            image = R.drawable.house,
            members = listOf(R.drawable.house, R.drawable.house, R.drawable.house),
            topics = listOf("Límites", "Derivadas", "Integrales", "Aplicaciones de Derivadas")
        ),
        Group(
            title = "Física Universitaria",
            description = "Aquí va una breve descripción del curso",
            image = R.drawable.house,
            members = listOf(R.drawable.house, R.drawable.house, R.drawable.house),
            topics = listOf("Límites", "Derivadas", "Integrales", "Aplicaciones de Derivadas")
        )
    )

    HomeScreen(groups)
}

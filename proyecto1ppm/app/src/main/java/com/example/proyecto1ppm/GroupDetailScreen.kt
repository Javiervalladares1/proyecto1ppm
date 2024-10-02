package com.example.proyecto1ppm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment // Importamos el paquete correcto para Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GroupDetailsScreen(group: StudyGroup, onBackClick: () -> Unit) {
    // Habilitamos el scroll vertical con rememberScrollState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Hacemos la columna scrollable
            .padding(16.dp) // Añadimos padding a todo el contenido
    ) {
        // Encabezado con botón de regresar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Se usa correctamente ahora
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = group.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de portada del grupo
        Image(
            painter = painterResource(group.image),
            contentDescription = group.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del grupo
        Text(
            text = "Descripción",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = group.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Temas Abordados
        Text(
            text = "Temas Abordados",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column {
            group.topics.forEach { topic ->
                Text(text = "- $topic", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apartado de reuniones de Zoom
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

        // Botones de acciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Enviar mensaje */ }) {
                Text("Enviar Mensaje")
            }
            Button(onClick = { /* Agregar miembro */ }) {
                Text("Agregar Miembro")
            }
            Button(onClick = { /* Salir del grupo */ }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text("Salir del Grupo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensajes recientes o publicaciones dentro del grupo
        Text(
            text = "Mensajes Recientes",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Placeholder para mensajes recientes
        Column {
            repeat(3) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Usuario $index", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Este es un mensaje reciente en el grupo.")
                    }
                }
            }
        }
    }
}

// Datos del grupo y vista previa
data class StudyGroup( // Cambié el nombre a StudyGroup para evitar conflictos
    val title: String,
    val description: String,
    val image: Int, // Int es correcto si estas imágenes existen en drawable
    val members: List<Int>, // Int es correcto para los IDs de recursos de imágenes en drawable
    val topics: List<String>
)

@Preview(showBackground = true)
@Composable
fun PreviewGroupDetailsScreen() {
    val group = StudyGroup( // Aquí también cambié a StudyGroup
        title = "Cálculo Diferencial",
        description = "Este grupo es para discutir y resolver ejercicios de Cálculo Diferencial. " +
                "Es un lugar donde los estudiantes pueden colaborar y ayudarse mutuamente " +
                "para mejorar sus habilidades y conocimientos en el curso.",
        image = R.drawable.house,  // Asegúrate de que esta imagen existe en tu carpeta res/drawable
        members = listOf(R.drawable.house, R.drawable.house, R.drawable.house, R.drawable.house),  // Lo mismo para los avatares
        topics = listOf("Límites", "Derivadas", "Integrales", "Aplicaciones de Derivadas")
    )
    GroupDetailsScreen(group = group, onBackClick = { /* Navigate back */ })
}

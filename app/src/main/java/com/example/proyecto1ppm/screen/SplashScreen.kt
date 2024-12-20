package com.example.proyecto1ppm.screen

// Imports necesarios
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.R
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme


class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto1ppmTheme() {
                SplashScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // Fondo con una imagen que cubre toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondosplash), // Asegúrate de tener esta imagen en drawable
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop, // Para que cubra toda la pantalla
            modifier = Modifier.fillMaxSize()
        )

        // Capa de color morado semitransparente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000050)) // Color morado con opacidad (88 es el valor en hexadecimal)
        )

        // Logo y texto centrados
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del logo (birrete)
            Image(
                painter = painterResource(id = R.drawable.gorro_graduacion), // Reemplaza con el ID del logo del birrete
                contentDescription = "StudyMatch Logo",
                modifier = Modifier.size(250.dp)
            )

            // Texto de "StudyMatch"
            Text(
                text = "StudyMatch",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000L) // Simula 3 segundos de delay
                navController.navigate("registration_screen") {
                    popUpTo("splash_screen") { inclusive = true }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Proyecto1ppmTheme() {
        SplashScreen(navController = rememberNavController())
    }
}

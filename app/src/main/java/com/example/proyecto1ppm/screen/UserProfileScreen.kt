package com.example.proyecto1ppm.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.Geocoder
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: ""

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var locationText by remember { mutableStateOf("Ubicación desconocida") }

    // Estados de permisos
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Launcher para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                avatarBitmap = it
                // Guardar el avatar localmente
                saveAvatarToInternalStorage(context, userId, it)
            }
        }
    }

    // Obtener datos del usuario
    LaunchedEffect(userId) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    firstName = document.getString("firstName") ?: ""
                    lastName = document.getString("lastName") ?: ""
                    fullName = "$firstName $lastName"
                    // Cargar avatar desde almacenamiento local
                    avatarBitmap = loadAvatarFromInternalStorage(context, userId)
                }
            }
            .addOnFailureListener {
                // Manejar el error si es necesario
            }
    }

    // Solicitar permisos si no están concedidos
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Obtener ubicación
    var location by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { locationResult ->
                locationResult?.let {
                    location = it
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val country = addresses[0].countryName
                            locationText = country ?: "Ubicación desconocida"
                        } else {
                            locationText = "${it.latitude}, ${it.longitude}"
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        locationText = "${it.latitude}, ${it.longitude}"
                    }
                }
            }
        }
    }

    // Construir la interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Icono de menú
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier
                .clickable { navController.navigate("slide_menu_screen") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    if (cameraPermissionState.status.isGranted) {
                        // Abre la cámara
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(cameraIntent)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap!!.asImageBitmap(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detalles personales
        Text(
            text = "Detalles Personales",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        UserDetailField(label = "Nombre Completo", value = fullName)
        UserDetailField(label = "Email", value = email)
        ChangeOptionText("Cambiar Email")

        UserDetailField(label = "Contraseña", value = "************", isPassword = true)
        ChangeOptionText("Cambiar Contraseña")

        Spacer(modifier = Modifier.height(16.dp))

        // Ubicación
        Text(
            text = "Ubicación",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (locationPermissionState.status.isGranted) {
            if (location != null) {
                Text(
                    text = "Ubicación: $locationText",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text("Obteniendo ubicación...")
            }
        } else {
            Text("Permiso de ubicación denegado.")
            Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                Text("Solicitar Permiso de Ubicación")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cursos asignados
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

// Función para guardar el avatar en el almacenamiento interno
fun saveAvatarToInternalStorage(context: Context, userId: String, bitmap: Bitmap): Boolean {
    return try {
        context.openFileOutput("$userId-avatar.png", Context.MODE_PRIVATE).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Función para cargar el avatar desde el almacenamiento interno
fun loadAvatarFromInternalStorage(context: Context, userId: String): Bitmap? {
    return try {
        val file = context.getFileStreamPath("$userId-avatar.png")
        if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

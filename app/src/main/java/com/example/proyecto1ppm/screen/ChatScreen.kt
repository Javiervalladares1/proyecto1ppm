package com.example.proyecto1ppm.screen

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import android.Manifest
import android.graphics.Bitmap
import androidx.activity.result.launch

data class ChatMessage(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)
@Composable
fun ChatScreen(navController: NavController, courseId: String) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: ""

    val messagesCollection = db.collection("groups").document(courseId).collection("messages")

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    // Obtener nombre y apellido del usuario y escuchar mensajes
    LaunchedEffect(userId) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    firstName = document.getString("firstName") ?: ""
                    lastName = document.getString("lastName") ?: ""
                } else {
                    firstName = "Usuario"
                    lastName = "Desconocido"
                }
            }
            .addOnFailureListener {
                firstName = "Usuario"
                lastName = "Desconocido"
            }

        // Escuchar mensajes en tiempo real
        messagesCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al obtener mensajes: ${error.message}")
                } else if (snapshot != null) {
                    val msgs = snapshot.documents.mapNotNull { it.toObject(ChatMessage::class.java) }
                    messages.clear()
                    messages.addAll(msgs)
                }
            }
    }

    val context = LocalContext.current
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Lanzador para solicitar permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted.value = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Lanzador para tomar fotos
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            // Convertir el Bitmap en un String para enviarlo al chat
            val photoMessage = ChatMessage(
                userId = userId,
                firstName = firstName,
                lastName = lastName,
                message = "📷 Foto enviada",
                timestamp = System.currentTimeMillis()
            )

            // Opcional: Subir la imagen a un almacenamiento y usar un enlace
            // Aquí se puede implementar la subida a Firebase Storage.

            messagesCollection.add(photoMessage)
        } else {
            Toast.makeText(context, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado con botón de regresar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White
                )
            }
            Text(
                text = "Chat del Curso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Lista de mensajes
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = false
        ) {
            items(messages) { msg ->
                val isCurrentUser = msg.userId == userId
                val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
                val backgroundColor = if (isCurrentUser) Color(0xFFD1C4E9) else Color(0xFFB39DDB)
                val textColor = Color.Black

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = alignment
                ) {
                    val senderName = if (isCurrentUser) "Tú" else "${msg.firstName} ${msg.lastName}"
                    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val timeString = dateFormat.format(Date(msg.timestamp))

                    Text(
                        text = "$senderName • $timeString",
                        style = TextStyle(color = Color.Gray, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = msg.message,
                        style = TextStyle(color = textColor),
                        modifier = Modifier
                            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                            .padding(8.dp)
                    )
                }
            }
        }

        // Desplazarse al último mensaje cuando la lista cambia
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        // Campo de entrada y botones
        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val newMessage = ChatMessage(
                            userId = userId,
                            firstName = firstName,
                            lastName = lastName,
                            message = messageText,
                            timestamp = System.currentTimeMillis()
                        )
                        messagesCollection.add(newMessage)
                        messageText = ""
                    }
                }
            ) {
                Text("Enviar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (!permissionGranted.value) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        takePictureLauncher.launch()
                    }
                }
            ) {
                Text("📷")
            }
        }
    }
}


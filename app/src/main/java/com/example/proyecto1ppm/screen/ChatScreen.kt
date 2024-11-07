package com.example.proyecto1ppm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class ChatMessage(
    val userId: String = "",
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

    // Escuchar mensajes en tiempo real
    LaunchedEffect(courseId) {
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

    Column(modifier = Modifier.fillMaxSize()) {
        // Lista de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp)
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

        // Campo de entrada y botón de enviar
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
        }
    }
}



package com.example.proyecto1ppm.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun getUserFullName(onResult: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    currentUser?.uid?.let { uid ->
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val fullName = "$firstName $lastName"
                    onResult(fullName)
                } else {
                    onResult("")
                }
            }
            .addOnFailureListener {
                onResult("")
            }
    } ?: onResult("")
}

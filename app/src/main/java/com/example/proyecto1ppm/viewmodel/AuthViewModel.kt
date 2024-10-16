package com.example.proyecto1ppm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto1ppm.data.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    fun checkCurrentUser() {
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val user = authRepository.signUpWithEmail(email, password)
            _currentUser.value = user
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val user = authRepository.signInWithEmail(email, password)
            _currentUser.value = user
        }
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
    }
}

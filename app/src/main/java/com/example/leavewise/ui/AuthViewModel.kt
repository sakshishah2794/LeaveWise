package com.example.leavewise.ui

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class MockUser(val email: String)

class AuthViewModel : ViewModel() {
    // Mock user state - starts as null (not logged in) or can be pre-set
    var currentUser = mutableStateOf<MockUser?>(MockUser("test@example.com"))
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (!isValidEmail(email)) {
            errorMessage.value = "Please enter a valid email address"
            return
        }
        if (password.length < 4) {
            errorMessage.value = "Password must be at least 4 characters"
            return
        }

        // Mock login success
        currentUser.value = MockUser(email)
        onSuccess()
    }

    fun signUp(email: String, password: String, confirmPass: String, onSuccess: () -> Unit) {
        if (!isValidEmail(email)) {
            errorMessage.value = "Please enter a valid email address"
            return
        }
        if (password.length < 4) {
            errorMessage.value = "Password must be at least 4 characters"
            return
        }
        if (password != confirmPass) {
            errorMessage.value = "Passwords do not match"
            return
        }

        // Mock signup success
        currentUser.value = MockUser(email)
        onSuccess()
    }

    fun logout(onSuccess: () -> Unit) {
        currentUser.value = null
        onSuccess()
    }

    fun clearError() {
        errorMessage.value = null
    }
}
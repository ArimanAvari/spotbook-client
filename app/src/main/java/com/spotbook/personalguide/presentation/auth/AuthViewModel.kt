package com.spotbook.personalguide.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var state by mutableStateOf(AuthState())
        private set

    fun onEmailChange(value: String) {
        state = state.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value, error = null)
    }

    fun onConfirmPasswordChange(value: String) {
        state = state.copy(confirmPassword = value, error = null)
    }

    fun login(): Boolean {
        return when {
            state.email.isBlank() -> fail("Введите email")
            state.password.isBlank() -> fail("Введите пароль")
            else -> {
                state = state.copy(isLoggedIn = true, error = null)
                true
            }
        }
    }

    fun register(): Boolean {
        return when {
            state.email.isBlank() -> fail("Введите email")
            state.password.length < 6 -> fail("Пароль должен быть не короче 6 символов")
            state.password != state.confirmPassword -> fail("Пароли не совпадают")
            else -> {
                state = state.copy(isLoggedIn = true, error = null)
                true
            }
        }
    }

    fun logout() {
        state = AuthState()
    }

    private fun fail(message: String): Boolean {
        state = state.copy(error = message)
        return false
    }
}


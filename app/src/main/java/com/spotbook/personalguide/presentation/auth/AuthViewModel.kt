package com.spotbook.personalguide.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spotbook.personalguide.domain.repository.AuthRepository
import com.spotbook.personalguide.domain.usecase.LoginUseCase
import com.spotbook.personalguide.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    private val loginUseCase = LoginUseCase(authRepository)
    private val registerUseCase = RegisterUseCase(authRepository)
    private val repository = authRepository

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

    fun login(onSuccess: suspend () -> Unit) {
        when {
            state.email.isBlank() -> fail("Введите email")
            state.password.isBlank() -> fail("Введите пароль")
            else -> viewModelScope.launch {
                state = state.copy(isLoading = true, error = null)
                try {
                    val user = loginUseCase(state.email, state.password)
                    state = state.copy(user = user, error = null)
                    onSuccess()
                    state = state.copy(isLoading = false)
                } catch (error: Throwable) {
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось войти"
                    )
                }
            }
        }
    }

    fun register(onSuccess: suspend () -> Unit) {
        when {
            state.email.isBlank() -> fail("Введите email")
            state.password.length < 6 -> fail("Пароль должен быть не короче 6 символов")
            state.password != state.confirmPassword -> fail("Пароли не совпадают")
            else -> viewModelScope.launch {
                state = state.copy(isLoading = true, error = null)
                try {
                    val user = registerUseCase(state.email, state.password)
                    state = state.copy(user = user, error = null)
                    onSuccess()
                    state = state.copy(isLoading = false)
                } catch (error: Throwable) {
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось зарегистрироваться"
                    )
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        state = AuthState()
    }

    private fun fail(message: String) {
        state = state.copy(error = message)
    }

    class Factory(
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}

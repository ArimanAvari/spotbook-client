package com.spotbook.personalguide.presentation.auth

import com.spotbook.personalguide.domain.model.User

data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

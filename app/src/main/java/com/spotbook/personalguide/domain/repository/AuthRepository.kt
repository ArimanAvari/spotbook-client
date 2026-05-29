package com.spotbook.personalguide.domain.repository

import com.spotbook.personalguide.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): User
    suspend fun getCurrentUser(): User
    fun logout()
    fun hasToken(): Boolean
}


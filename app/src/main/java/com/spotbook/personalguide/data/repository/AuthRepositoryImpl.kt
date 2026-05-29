package com.spotbook.personalguide.data.repository

import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.token.TokenStorage
import com.spotbook.personalguide.domain.model.User
import com.spotbook.personalguide.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = apiService.login(email.trim(), password)
        tokenStorage.saveToken(response.token)
        return response.user.toDomain()
    }

    override suspend fun register(email: String, password: String): User {
        val response = apiService.register(email.trim(), password)
        tokenStorage.saveToken(response.token)
        return response.user.toDomain()
    }

    override suspend fun getCurrentUser(): User {
        return apiService.me().toDomain()
    }

    override fun logout() {
        tokenStorage.clearToken()
    }

    override fun hasToken(): Boolean {
        return tokenStorage.getToken() != null
    }

    private fun com.spotbook.personalguide.data.remote.UserDto.toDomain(): User {
        return User(id = id, email = email)
    }
}


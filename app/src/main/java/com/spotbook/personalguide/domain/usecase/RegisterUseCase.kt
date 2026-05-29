package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.register(email, password)
}


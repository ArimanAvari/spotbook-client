package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}


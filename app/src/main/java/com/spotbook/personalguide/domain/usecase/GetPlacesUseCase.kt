package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.PlaceRepository

class GetPlacesUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke() = repository.getRemotePlaces()
}


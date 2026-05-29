package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.data.remote.PlaceRequestDto
import com.spotbook.personalguide.domain.repository.PlaceRepository

class CreatePlaceUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(request: PlaceRequestDto) = repository.createRemotePlace(request)
}


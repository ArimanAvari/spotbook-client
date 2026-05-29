package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.data.remote.PlaceRequestDto
import com.spotbook.personalguide.domain.repository.PlaceRepository

class UpdatePlaceUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(serverId: Long, request: PlaceRequestDto) = repository.updateRemotePlace(serverId, request)
}


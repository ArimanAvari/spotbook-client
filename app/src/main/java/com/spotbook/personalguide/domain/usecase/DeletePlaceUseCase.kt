package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.PlaceRepository

class DeletePlaceUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(serverId: Long) = repository.deleteRemotePlace(serverId)
}


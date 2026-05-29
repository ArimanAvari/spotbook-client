package com.spotbook.personalguide.data.repository

import com.spotbook.personalguide.data.mapper.toDomain
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.remote.PlaceRequestDto
import com.spotbook.personalguide.data.remote.UpdatePlaceStatusRequestDto
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val apiService: ApiService
) : PlaceRepository {
    override suspend fun getRemotePlaces(): List<PlaceCard> {
        return apiService.getPlaces().map { it.toDomain() }
    }

    override suspend fun createRemotePlace(request: PlaceRequestDto): PlaceCard {
        return apiService.createPlace(request).toDomain()
    }

    override suspend fun updateRemotePlace(serverId: Long, request: PlaceRequestDto): PlaceCard {
        return apiService.updatePlace(serverId, request).toDomain()
    }

    override suspend fun deleteRemotePlace(serverId: Long) {
        apiService.deletePlace(serverId)
    }

    override suspend fun updateRemoteStatus(serverId: Long, status: PlaceStatus): PlaceCard {
        return apiService.updatePlaceStatus(serverId, UpdatePlaceStatusRequestDto(status)).toDomain()
    }
}


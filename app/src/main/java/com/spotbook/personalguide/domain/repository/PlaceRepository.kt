package com.spotbook.personalguide.domain.repository

import com.spotbook.personalguide.data.remote.PlaceRequestDto
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.PlaceStatus

interface PlaceRepository {
    suspend fun getRemotePlaces(): List<PlaceCard>
    suspend fun createRemotePlace(request: PlaceRequestDto): PlaceCard
    suspend fun updateRemotePlace(serverId: Long, request: PlaceRequestDto): PlaceCard
    suspend fun deleteRemotePlace(serverId: Long)
    suspend fun updateRemoteStatus(serverId: Long, status: PlaceStatus): PlaceCard
}


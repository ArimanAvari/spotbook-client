package com.spotbook.personalguide.data.repository

import com.spotbook.personalguide.data.mapper.toDomain
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.remote.GroupRequestDto
import com.spotbook.personalguide.domain.model.Group
import com.spotbook.personalguide.domain.repository.GroupRepository

class GroupRepositoryImpl(
    private val apiService: ApiService
) : GroupRepository {
    override suspend fun getRemoteGroups(): List<Group> {
        return apiService.getGroups().map { it.toDomain() }
    }

    override suspend fun createRemoteGroup(name: String): Group {
        return apiService.createGroup(GroupRequestDto(name)).toDomain()
    }

    override suspend fun deleteRemoteGroup(serverId: Long) {
        apiService.deleteGroup(serverId)
    }

    override suspend fun addPlaceToRemoteGroup(groupServerId: Long, placeServerId: Long) {
        apiService.addPlaceToGroup(groupServerId, placeServerId)
    }

    override suspend fun removePlaceFromRemoteGroup(groupServerId: Long, placeServerId: Long) {
        apiService.removePlaceFromGroup(groupServerId, placeServerId)
    }
}


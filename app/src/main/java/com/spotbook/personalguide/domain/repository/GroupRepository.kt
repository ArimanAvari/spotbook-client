package com.spotbook.personalguide.domain.repository

import com.spotbook.personalguide.domain.model.Group

interface GroupRepository {
    suspend fun getRemoteGroups(): List<Group>
    suspend fun createRemoteGroup(name: String): Group
    suspend fun deleteRemoteGroup(serverId: Long)
    suspend fun addPlaceToRemoteGroup(groupServerId: Long, placeServerId: Long)
    suspend fun removePlaceFromRemoteGroup(groupServerId: Long, placeServerId: Long)
}


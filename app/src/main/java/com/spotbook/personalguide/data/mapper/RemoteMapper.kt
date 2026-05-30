package com.spotbook.personalguide.data.mapper

import com.spotbook.personalguide.data.remote.GroupDto
import com.spotbook.personalguide.data.remote.PlaceDto
import com.spotbook.personalguide.domain.model.Group
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.SyncStatus

fun PlaceDto.toDomain(): PlaceCard {
    return PlaceCard(
        localId = 0,
        serverId = id,
        title = title,
        address = address,
        photoPath = null,
        serverPhotoPath = photoPath,
        rating = rating,
        comment = comment,
        status = status,
        groupId = groupId,
        syncStatus = SyncStatus.SYNCED,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun GroupDto.toDomain(): Group {
    return Group(
        localId = 0,
        serverId = id,
        name = name,
        syncStatus = SyncStatus.SYNCED,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

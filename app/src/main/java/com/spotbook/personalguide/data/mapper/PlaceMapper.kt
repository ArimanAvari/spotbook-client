package com.spotbook.personalguide.data.mapper

import com.spotbook.personalguide.data.local.PlaceEntity
import com.spotbook.personalguide.domain.model.PlaceCard

fun PlaceEntity.toDomain(): PlaceCard {
    return PlaceCard(
        localId = localId,
        serverId = serverId,
        title = title,
        address = address,
        photoPath = photoPath,
        serverPhotoPath = serverPhotoPath,
        rating = rating,
        comment = comment,
        status = status,
        groupId = groupId,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun PlaceCard.toEntity(): PlaceEntity {
    return PlaceEntity(
        localId = localId,
        serverId = serverId,
        title = title,
        address = address,
        photoPath = photoPath,
        serverPhotoPath = serverPhotoPath,
        rating = rating,
        comment = comment,
        status = status,
        groupId = groupId,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

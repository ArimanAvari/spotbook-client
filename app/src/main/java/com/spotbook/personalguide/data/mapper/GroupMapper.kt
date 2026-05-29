package com.spotbook.personalguide.data.mapper

import com.spotbook.personalguide.data.local.GroupEntity
import com.spotbook.personalguide.domain.model.Group

fun GroupEntity.toDomain(): Group {
    return Group(
        localId = localId,
        serverId = serverId,
        name = name,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Group.toEntity(): GroupEntity {
    return GroupEntity(
        localId = localId,
        serverId = serverId,
        name = name,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


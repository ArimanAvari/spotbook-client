package com.spotbook.personalguide.domain.model

data class Group(
    val localId: Long,
    val serverId: Long?,
    val name: String,
    val syncStatus: SyncStatus,
    val createdAt: String,
    val updatedAt: String
)


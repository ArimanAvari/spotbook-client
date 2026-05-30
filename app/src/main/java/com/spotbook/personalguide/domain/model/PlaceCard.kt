package com.spotbook.personalguide.domain.model

data class PlaceCard(
    val localId: Long,
    val serverId: Long?,
    val title: String,
    val address: String,
    val photoPath: String?,
    val serverPhotoPath: String?,
    val rating: Int,
    val comment: String,
    val status: PlaceStatus,
    val groupId: Long?,
    val syncStatus: SyncStatus,
    val createdAt: String,
    val updatedAt: String
)

package com.spotbook.personalguide.data.remote

import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.model.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
data class SyncExportRequestDto(
    val groups: List<SyncGroupItemDto> = emptyList(),
    val places: List<SyncPlaceItemDto> = emptyList()
)

@Serializable
data class SyncGroupItemDto(
    val localId: Long,
    val serverId: Long? = null,
    val name: String,
    val syncStatus: SyncStatus,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class SyncPlaceItemDto(
    val localId: Long,
    val serverId: Long? = null,
    val title: String,
    val address: String,
    val photoPath: String? = null,
    val rating: Int,
    val comment: String,
    val status: PlaceStatus,
    val groupLocalId: Long? = null,
    val groupServerId: Long? = null,
    val syncStatus: SyncStatus,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class SyncExportResponseDto(
    val groups: List<SyncIdMappingDto>,
    val places: List<SyncIdMappingDto>,
    val data: SyncImportResponseDto
)

@Serializable
data class SyncIdMappingDto(
    val localId: Long,
    val serverId: Long? = null
)

@Serializable
data class SyncImportResponseDto(
    val groups: List<ServerGroupDto>,
    val places: List<ServerPlaceDto>
)

@Serializable
data class ServerGroupDto(
    val serverId: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class ServerPlaceDto(
    val serverId: Long,
    val title: String,
    val address: String,
    val photoPath: String? = null,
    val rating: Int,
    val comment: String,
    val status: PlaceStatus,
    val groupServerId: Long? = null,
    val createdAt: String,
    val updatedAt: String
)


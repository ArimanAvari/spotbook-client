package com.spotbook.personalguide.data.repository

import com.spotbook.personalguide.data.local.GroupDao
import com.spotbook.personalguide.data.local.GroupEntity
import com.spotbook.personalguide.data.local.PlaceDao
import com.spotbook.personalguide.data.local.PlaceEntity
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.remote.ServerGroupDto
import com.spotbook.personalguide.data.remote.ServerPlaceDto
import com.spotbook.personalguide.data.remote.SyncExportRequestDto
import com.spotbook.personalguide.data.remote.SyncGroupItemDto
import com.spotbook.personalguide.data.remote.SyncImportResponseDto
import com.spotbook.personalguide.data.remote.SyncPlaceItemDto
import com.spotbook.personalguide.domain.model.SyncStatus
import com.spotbook.personalguide.domain.repository.SyncRepository
import java.io.File
import java.time.Instant

class SyncRepositoryImpl(
    private val apiService: ApiService,
    private val placeDao: PlaceDao,
    private val groupDao: GroupDao
) : SyncRepository {
    override suspend fun exportData() {
        val dirtyGroups = groupDao.getGroupsBySyncStatus(
            listOf(SyncStatus.NOT_SYNCED, SyncStatus.DELETED)
        )
        val dirtyPlaces = placeDao.getPlacesBySyncStatus(
            listOf(SyncStatus.NOT_SYNCED, SyncStatus.DELETED)
        )
        val groupServerIds = dirtyPlaces
            .mapNotNull { it.groupId }
            .distinct()
            .associateWith { localGroupId -> groupDao.getGroupByLocalId(localGroupId)?.serverId }

        val response = apiService.exportData(
            SyncExportRequestDto(
                groups = dirtyGroups.map { it.toSyncDto() },
                places = dirtyPlaces.map { it.toSyncDto(groupServerIds[it.groupId]) }
            )
        )

        uploadLocalPhotos(dirtyPlaces, response.places)
        replaceImportedData(apiService.importData())
    }

    override suspend fun importData() {
        mergeImportedData(apiService.importData())
    }

    private suspend fun replaceImportedData(data: SyncImportResponseDto) {
        placeDao.clearPlaces()
        groupDao.clearGroups()

        val groupLocalIdsByServerId = mutableMapOf<Long, Long>()
        data.groups.forEach { group ->
            val localId = groupDao.insertGroup(group.toEntity())
            groupLocalIdsByServerId[group.serverId] = localId
        }

        placeDao.insertPlaces(
            data.places.map { place ->
                place.toEntity(groupLocalIdsByServerId[place.groupServerId])
            }
        )
    }

    private suspend fun mergeImportedData(data: SyncImportResponseDto) {
        val groupLocalIdsByServerId = mutableMapOf<Long, Long>()

        data.groups.forEach { group ->
            val existing = groupDao.getGroupByServerId(group.serverId)
            val localId = when {
                existing == null -> {
                    groupDao.insertGroup(group.toEntity())
                }

                existing.syncStatus.isLocalChange() -> {
                    existing.localId
                }

                isServerNewer(group.updatedAt, existing.updatedAt) -> {
                    groupDao.updateGroup(group.toEntity().copy(localId = existing.localId))
                    existing.localId
                }

                else -> existing.localId
            }

            if (existing?.syncStatus != SyncStatus.DELETED) {
                groupLocalIdsByServerId[group.serverId] = localId
            }
        }

        data.places.forEach { place ->
            val existing = placeDao.getPlaceByServerId(place.serverId)
            val localGroupId = place.groupServerId?.let { groupServerId ->
                groupLocalIdsByServerId[groupServerId]
                    ?: groupDao.getGroupByServerId(groupServerId)
                        ?.takeIf { it.syncStatus != SyncStatus.DELETED }
                        ?.localId
            }

            when {
                existing == null -> {
                    placeDao.insertPlace(place.toEntity(localGroupId))
                }

                existing.syncStatus.isLocalChange() -> {
                    return@forEach
                }

                isServerNewer(place.updatedAt, existing.updatedAt) -> {
                    placeDao.updatePlace(place.toEntity(localGroupId).copy(localId = existing.localId))
                }
            }
        }
    }

    private fun GroupEntity.toSyncDto(): SyncGroupItemDto {
        return SyncGroupItemDto(
            localId = localId,
            serverId = serverId,
            name = name,
            syncStatus = syncStatus,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun PlaceEntity.toSyncDto(groupServerId: Long?): SyncPlaceItemDto {
        return SyncPlaceItemDto(
            localId = localId,
            serverId = serverId,
            title = title,
            address = address,
            photoPath = photoPath.takeIf { it.isServerPhotoPath() },
            rating = rating,
            comment = comment,
            status = status,
            groupLocalId = groupId,
            groupServerId = groupServerId,
            syncStatus = syncStatus,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private suspend fun uploadLocalPhotos(
        places: List<PlaceEntity>,
        mappings: List<com.spotbook.personalguide.data.remote.SyncIdMappingDto>
    ) {
        val serverIdsByLocalId = mappings
            .mapNotNull { mapping -> mapping.serverId?.let { mapping.localId to it } }
            .toMap()

        places
            .filter { it.syncStatus != SyncStatus.DELETED }
            .forEach { place ->
                val photoPath = place.photoPath ?: return@forEach
                val photoFile = File(photoPath)
                val serverId = serverIdsByLocalId[place.localId] ?: place.serverId ?: return@forEach
                if (photoFile.exists() && photoFile.isFile) {
                    apiService.uploadPlacePhoto(serverId, photoFile)
                }
            }
    }

    private fun String?.isServerPhotoPath(): Boolean {
        return this != null && (
            startsWith("uploads/") ||
                startsWith("http://") ||
                startsWith("https://")
            )
    }

    private fun SyncStatus.isLocalChange(): Boolean {
        return this == SyncStatus.NOT_SYNCED || this == SyncStatus.DELETED
    }

    private fun isServerNewer(serverUpdatedAt: String, localUpdatedAt: String): Boolean {
        return runCatching {
            Instant.parse(serverUpdatedAt).isAfter(Instant.parse(localUpdatedAt))
        }.getOrElse {
            serverUpdatedAt > localUpdatedAt
        }
    }

    private fun ServerGroupDto.toEntity(): GroupEntity {
        return GroupEntity(
            serverId = serverId,
            name = name,
            syncStatus = SyncStatus.SYNCED,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun ServerPlaceDto.toEntity(localGroupId: Long?): PlaceEntity {
        return PlaceEntity(
            serverId = serverId,
            title = title,
            address = address,
            photoPath = photoPath,
            rating = rating,
            comment = comment,
            status = status,
            groupId = localGroupId,
            syncStatus = SyncStatus.SYNCED,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

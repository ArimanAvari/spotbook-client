package com.spotbook.personalguide.data.repository

import com.spotbook.personalguide.data.local.GroupDao
import com.spotbook.personalguide.data.local.PlaceDao
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.domain.repository.SyncRepository

class SyncRepositoryImpl(
    private val apiService: ApiService,
    private val placeDao: PlaceDao,
    private val groupDao: GroupDao
) : SyncRepository {
    override suspend fun exportData() {
        apiService.exportData(
            request = com.spotbook.personalguide.data.remote.SyncExportRequestDto()
        )
    }

    override suspend fun importData() {
        apiService.importData()
    }
}


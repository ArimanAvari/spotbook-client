package com.spotbook.personalguide.domain.repository

interface SyncRepository {
    suspend fun exportData()
    suspend fun importData()
    suspend fun replaceDataFromServer()
}

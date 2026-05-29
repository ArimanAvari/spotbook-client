package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.SyncRepository

class ExportDataUseCase(private val repository: SyncRepository) {
    suspend operator fun invoke() = repository.exportData()
}


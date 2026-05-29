package com.spotbook.personalguide.domain.usecase

import com.spotbook.personalguide.domain.repository.SyncRepository

class ImportDataUseCase(private val repository: SyncRepository) {
    suspend operator fun invoke() = repository.importData()
}


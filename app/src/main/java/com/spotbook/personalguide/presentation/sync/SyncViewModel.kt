package com.spotbook.personalguide.presentation.sync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spotbook.personalguide.domain.repository.SyncRepository
import com.spotbook.personalguide.domain.usecase.ExportDataUseCase
import com.spotbook.personalguide.domain.usecase.ImportDataUseCase
import kotlinx.coroutines.launch

data class SyncState(
    val statusText: String = "Синхронизация ещё не запускалась",
    val isLoading: Boolean = false,
    val error: String? = null
)

class SyncViewModel(
    syncRepository: SyncRepository
) : ViewModel() {
    private val exportDataUseCase = ExportDataUseCase(syncRepository)
    private val importDataUseCase = ImportDataUseCase(syncRepository)

    var state by mutableStateOf(SyncState())
        private set

    fun exportData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, statusText = "Экспорт выполняется")
            runCatching {
                exportDataUseCase()
            }.onSuccess {
                state = SyncState(statusText = "Экспорт завершён, локальная база обновлена")
            }.onFailure { error ->
                state = SyncState(
                    statusText = "Экспорт не выполнен",
                    error = error.message ?: "Ошибка экспорта"
                )
            }
        }
    }

    fun importData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, statusText = "Импорт выполняется")
            runCatching {
                importDataUseCase()
            }.onSuccess {
                state = SyncState(statusText = "Импорт завершён, данные сохранены в Room")
            }.onFailure { error ->
                state = SyncState(
                    statusText = "Импорт не выполнен",
                    error = error.message ?: "Ошибка импорта"
                )
            }
        }
    }

    class Factory(
        private val syncRepository: SyncRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SyncViewModel(syncRepository) as T
        }
    }
}


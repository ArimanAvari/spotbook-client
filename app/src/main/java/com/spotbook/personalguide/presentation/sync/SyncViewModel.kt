package com.spotbook.personalguide.presentation.sync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class SyncState(
    val statusText: String = "Синхронизация ещё не запускалась",
    val isLoading: Boolean = false,
    val error: String? = null
)

class SyncViewModel : ViewModel() {
    var state by mutableStateOf(SyncState())
        private set

    fun exportData() {
        state = SyncState(statusText = "Экспорт будет подключён к backend на следующем этапе")
    }

    fun importData() {
        state = SyncState(statusText = "Импорт будет подключён к backend на следующем этапе")
    }
}


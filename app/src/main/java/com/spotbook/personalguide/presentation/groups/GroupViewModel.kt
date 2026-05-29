package com.spotbook.personalguide.presentation.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spotbook.personalguide.data.local.GroupDao
import com.spotbook.personalguide.data.local.GroupEntity
import com.spotbook.personalguide.data.local.PlaceDao
import com.spotbook.personalguide.data.mapper.toDomain
import com.spotbook.personalguide.domain.model.Group
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.SyncStatus
import java.time.Instant
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class GroupState(
    val groups: List<Group> = emptyList(),
    val places: List<PlaceCard> = emptyList(),
    val newGroupName: String = "",
    val error: String? = null
)

class GroupViewModel(
    private val groupDao: GroupDao,
    private val placeDao: PlaceDao
) : ViewModel() {
    var state by mutableStateOf(GroupState())
        private set

    init {
        viewModelScope.launch {
            combine(groupDao.observeGroups(), placeDao.observePlaces()) { groups, places ->
                state.copy(
                    groups = groups.map { it.toDomain() },
                    places = places.map { it.toDomain() }
                )
            }.collect { state = it }
        }
    }

    fun onNewGroupNameChange(value: String) {
        state = state.copy(newGroupName = value, error = null)
    }

    fun createGroup() {
        if (state.newGroupName.isBlank()) {
            state = state.copy(error = "Введите название группы")
            return
        }

        viewModelScope.launch {
            val now = Instant.now().toString()
            groupDao.insertGroup(
                GroupEntity(
                    serverId = null,
                    name = state.newGroupName.trim(),
                    syncStatus = SyncStatus.NOT_SYNCED,
                    createdAt = now,
                    updatedAt = now
                )
            )
            state = state.copy(newGroupName = "")
        }
    }

    fun deleteGroup(localId: Long) {
        viewModelScope.launch {
            val group = groupDao.getGroupByLocalId(localId) ?: return@launch
            state.places
                .filter { it.groupId == localId }
                .forEach { placeDao.removePlaceFromGroup(it.localId) }

            if (group.serverId == null) {
                groupDao.deleteGroup(group)
            } else {
                groupDao.updateGroup(group.copy(syncStatus = SyncStatus.DELETED))
            }
        }
    }

    class Factory(
        private val groupDao: GroupDao,
        private val placeDao: PlaceDao
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupViewModel(groupDao, placeDao) as T
        }
    }
}


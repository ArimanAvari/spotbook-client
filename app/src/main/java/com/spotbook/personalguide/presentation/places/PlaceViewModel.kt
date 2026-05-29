package com.spotbook.personalguide.presentation.places

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spotbook.personalguide.data.local.GroupDao
import com.spotbook.personalguide.data.local.PlaceDao
import com.spotbook.personalguide.data.local.PlaceEntity
import com.spotbook.personalguide.data.mapper.toDomain
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.model.SyncStatus
import java.time.Instant
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PlaceViewModel(
    private val placeDao: PlaceDao,
    private val groupDao: GroupDao
) : ViewModel() {
    var state by mutableStateOf(PlaceState())
        private set

    var formState by mutableStateOf(PlaceFormState())
        private set

    init {
        viewModelScope.launch {
            combine(placeDao.observePlaces(), groupDao.observeGroups()) { places, groups ->
                PlaceState(
                    places = places.map { it.toDomain() },
                    groups = groups.map { it.toDomain() }
                )
            }.collect { state = it }
        }
    }

    fun startCreate() {
        formState = PlaceFormState()
    }

    fun startEdit(localId: Long) {
        viewModelScope.launch {
            val place = placeDao.getPlaceByLocalId(localId) ?: return@launch
            formState = PlaceFormState(
                title = place.title,
                address = place.address,
                comment = place.comment,
                rating = place.rating,
                status = place.status,
                groupId = place.groupId,
                photoPath = place.photoPath.orEmpty()
            )
        }
    }

    fun onTitleChange(value: String) {
        formState = formState.copy(title = value)
    }

    fun onAddressChange(value: String) {
        formState = formState.copy(address = value)
    }

    fun onCommentChange(value: String) {
        formState = formState.copy(comment = value)
    }

    fun onPhotoPathChange(value: String) {
        formState = formState.copy(photoPath = value)
    }

    fun onRatingChange(value: Int) {
        formState = formState.copy(rating = value)
    }

    fun onStatusChange(value: PlaceStatus) {
        formState = formState.copy(status = value)
    }

    fun onGroupChange(value: Long?) {
        formState = formState.copy(groupId = value)
    }

    fun savePlace(localId: Long?, onSaved: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            val now = Instant.now().toString()
            if (localId == null) {
                placeDao.insertPlace(
                    PlaceEntity(
                        serverId = null,
                        title = formState.title.trim(),
                        address = formState.address.trim(),
                        photoPath = formState.photoPath.ifBlank { null },
                        rating = formState.rating,
                        comment = formState.comment.trim(),
                        status = formState.status,
                        groupId = formState.groupId,
                        syncStatus = SyncStatus.NOT_SYNCED,
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val old = placeDao.getPlaceByLocalId(localId) ?: return@launch
                placeDao.updatePlace(
                    old.copy(
                        title = formState.title.trim(),
                        address = formState.address.trim(),
                        photoPath = formState.photoPath.ifBlank { null },
                        rating = formState.rating,
                        comment = formState.comment.trim(),
                        status = formState.status,
                        groupId = formState.groupId,
                        syncStatus = SyncStatus.NOT_SYNCED,
                        updatedAt = now
                    )
                )
            }
            onSaved()
        }
    }

    fun deletePlace(localId: Long, onDeleted: () -> Unit) {
        viewModelScope.launch {
            val place = placeDao.getPlaceByLocalId(localId) ?: return@launch
            if (place.serverId == null) {
                placeDao.deletePlace(place)
            } else {
                placeDao.updatePlace(place.copy(syncStatus = SyncStatus.DELETED))
            }
            onDeleted()
        }
    }

    fun removeFromGroup(localId: Long) {
        viewModelScope.launch {
            placeDao.removePlaceFromGroup(localId)
        }
    }

    private fun validateForm(): Boolean {
        val message = when {
            formState.title.isBlank() -> "Введите название"
            formState.address.isBlank() -> "Введите адрес"
            formState.rating !in 1..5 -> "Оценка должна быть от 1 до 5"
            else -> null
        }
        state = state.copy(error = message)
        return message == null
    }

    class Factory(
        private val placeDao: PlaceDao,
        private val groupDao: GroupDao
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaceViewModel(placeDao, groupDao) as T
        }
    }
}


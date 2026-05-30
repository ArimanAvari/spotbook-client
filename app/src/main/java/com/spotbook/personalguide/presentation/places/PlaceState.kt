package com.spotbook.personalguide.presentation.places

import com.spotbook.personalguide.domain.model.Group
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.PlaceStatus

data class PlaceState(
    val places: List<PlaceCard> = emptyList(),
    val groups: List<Group> = emptyList(),
    val error: String? = null
)

data class PlaceFormState(
    val title: String = "",
    val address: String = "",
    val comment: String = "",
    val rating: Int = 3,
    val status: PlaceStatus = PlaceStatus.WANT_TO_VISIT,
    val groupId: Long? = null,
    val photoPath: String = "",
    val serverPhotoPath: String = ""
)

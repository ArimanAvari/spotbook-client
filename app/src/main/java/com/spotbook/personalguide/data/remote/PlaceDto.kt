package com.spotbook.personalguide.data.remote

import com.spotbook.personalguide.domain.model.PlaceStatus
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: Long,
    val title: String,
    val address: String,
    val photoPath: String? = null,
    val rating: Int,
    val comment: String,
    val status: PlaceStatus,
    val groupId: Long? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class PlaceRequestDto(
    val title: String,
    val address: String,
    val rating: Int,
    val comment: String,
    val status: PlaceStatus,
    val groupId: Long? = null
)

@Serializable
data class UpdatePlaceStatusRequestDto(
    val status: PlaceStatus
)

@Serializable
data class PhotoUploadResponseDto(
    val photoPath: String
)

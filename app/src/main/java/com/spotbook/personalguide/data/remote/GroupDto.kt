package com.spotbook.personalguide.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class GroupRequestDto(
    val name: String
)

@Serializable
data class GroupPlacesResponseDto(
    val group: GroupDto,
    val places: List<PlaceDto>
)


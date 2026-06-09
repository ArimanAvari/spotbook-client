package com.spotbook.personalguide.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PhotoUploadResponseDto(
    val photoPath: String
)

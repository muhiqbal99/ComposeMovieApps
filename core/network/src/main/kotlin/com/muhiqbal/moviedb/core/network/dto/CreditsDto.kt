package com.muhiqbal.moviedb.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastDto(
    val id: Int? = null,
    val name: String? = null,
    val character: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int? = null,
)

@Serializable
data class CreditsResponseDto(
    val id: Int? = null,
    val cast: List<CastDto>? = null,
)

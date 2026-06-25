package com.muhiqbal.moviedb.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int? = null,
    val name: String? = null,
)

@Serializable
data class GenreListResponseDto(
    val genres: List<GenreDto>? = null,
)

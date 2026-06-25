package com.muhiqbal.moviedb.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val id: String? = null,
    val key: String? = null,
    val name: String? = null,
    val site: String? = null,
    val type: String? = null,
)

@Serializable
data class VideosResponseDto(
    val id: Int? = null,
    val results: List<VideoDto>? = null,
)

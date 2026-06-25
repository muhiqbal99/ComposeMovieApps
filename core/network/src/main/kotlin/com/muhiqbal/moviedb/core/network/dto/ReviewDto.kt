package com.muhiqbal.moviedb.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: String? = null,
    val author: String? = null,
    val content: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("author_details") val authorDetails: AuthorDetailsDto? = null,
)

@Serializable
data class AuthorDetailsDto(
    val rating: Double? = null,
)

@Serializable
data class PagedReviewsResponseDto(
    val id: Int? = null,
    val page: Int? = null,
    val results: List<ReviewDto>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null,
)

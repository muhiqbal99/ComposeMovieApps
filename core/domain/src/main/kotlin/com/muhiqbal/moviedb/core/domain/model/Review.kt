package com.muhiqbal.moviedb.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val rating: Double?,
)

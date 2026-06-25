package com.muhiqbal.moviedb.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
)

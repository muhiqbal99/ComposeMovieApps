package com.muhiqbal.moviedb.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?,
)

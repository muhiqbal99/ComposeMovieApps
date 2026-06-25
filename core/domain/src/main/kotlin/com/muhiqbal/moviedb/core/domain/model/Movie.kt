package com.muhiqbal.moviedb.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val genreIds: List<Int>,
)

package com.muhiqbal.moviedb.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int?,
    val genres: List<Genre>,
    val status: String,
)

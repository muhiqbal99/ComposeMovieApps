package com.muhiqbal.moviedb.feature.movie.list

import androidx.compose.runtime.Immutable

@Immutable
data class MovieListUiState(
    val genreId: Int = -1,
    val isInitialized: Boolean = false,
)

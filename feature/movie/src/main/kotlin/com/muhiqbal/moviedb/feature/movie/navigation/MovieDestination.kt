package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Immutable
sealed interface MovieDestination : NavKey {
    @Serializable
    data class MovieList(val genreId: Int, val genreName: String) : MovieDestination

    @Serializable
    data class MovieDetail(val movieId: Int) : MovieDestination
}

package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Immutable
sealed interface SearchDestination : NavKey {
    @Serializable
    data object Search : SearchDestination

    @Serializable
    data class MovieDetail(val movieId: Int) : SearchDestination
}

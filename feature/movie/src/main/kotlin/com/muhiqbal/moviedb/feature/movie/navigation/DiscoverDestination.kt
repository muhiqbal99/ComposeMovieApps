package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Immutable
sealed interface DiscoverDestination : NavKey {
    @Serializable
    data object DiscoverList : DiscoverDestination

    @Serializable
    data class MovieDetail(val movieId: Int) : DiscoverDestination
}

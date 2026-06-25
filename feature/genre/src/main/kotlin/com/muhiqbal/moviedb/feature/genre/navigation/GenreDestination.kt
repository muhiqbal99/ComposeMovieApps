package com.muhiqbal.moviedb.feature.genre.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Immutable
sealed interface GenreDestination : NavKey {
    @Serializable
    data object GenreList : GenreDestination
}

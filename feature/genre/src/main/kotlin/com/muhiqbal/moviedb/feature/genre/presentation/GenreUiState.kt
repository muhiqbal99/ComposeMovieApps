package com.muhiqbal.moviedb.feature.genre.presentation

import androidx.compose.runtime.Immutable
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.model.Movie

@Immutable
data class GenreSection(
    val genre: Genre,
    val movies: List<Movie>,
)

@Immutable
sealed interface GenreUiState {
    data object Loading : GenreUiState
    data class Success(val sections: List<GenreSection>) : GenreUiState
    data class Error(val error: Throwable?) : GenreUiState
}

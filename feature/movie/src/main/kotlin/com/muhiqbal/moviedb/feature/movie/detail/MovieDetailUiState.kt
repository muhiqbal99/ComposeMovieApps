package com.muhiqbal.moviedb.feature.movie.detail

import androidx.compose.runtime.Immutable
import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Video

@Immutable
sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(
        val movie: MovieDetail,
        val trailer: Video?,
        val cast: List<Cast> = emptyList(),
    ) : MovieDetailUiState
    data class Error(val error: Throwable?) : MovieDetailUiState
}

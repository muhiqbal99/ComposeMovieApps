package com.muhiqbal.moviedb.feature.movie.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val movieIdFlow = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviews: Flow<PagingData<Review>> = movieIdFlow
        .flatMapLatest { movieId ->
            if (movieId == -1) flow { emit(PagingData.empty()) }
            else movieRepository.getReviews(movieId)
        }
        .cachedIn(viewModelScope)

    fun setMovieId(movieId: Int) {
        if (movieIdFlow.value == movieId) return
        movieIdFlow.value = movieId
        loadMovieDetail(movieId)
    }

    private fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading
            val detailResult = movieRepository.getMovieDetail(movieId)
            val videosResult = movieRepository.getVideos(movieId)
            val creditsResult = movieRepository.getCredits(movieId)

            detailResult
                .onSuccess { detail ->
                    val trailer = videosResult.getOrNull()
                        ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && it.key.isNotBlank() }
                        ?: videosResult.getOrNull()
                            ?.firstOrNull { it.site == "YouTube" && it.key.isNotBlank() }
                    val cast = creditsResult.getOrNull().orEmpty().take(15)
                    _uiState.value = MovieDetailUiState.Success(
                        movie = detail,
                        trailer = trailer,
                        cast = cast,
                    )
                }
                .onFailure { error ->
                    _uiState.value = MovieDetailUiState.Error(error)
                }
        }
    }

    fun retry() {
        val movieId = movieIdFlow.value
        if (movieId != -1) loadMovieDetail(movieId)
    }
}

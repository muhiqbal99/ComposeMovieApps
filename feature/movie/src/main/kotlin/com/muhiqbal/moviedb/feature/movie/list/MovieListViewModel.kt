package com.muhiqbal.moviedb.feature.movie.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    private val genreIdFlow = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies: Flow<PagingData<Movie>> = genreIdFlow
        .flatMapLatest { genreId ->
            if (genreId == -1) flow { emit(PagingData.empty()) }
            else movieRepository.getMoviesByGenre(genreId)
        }
        .cachedIn(viewModelScope)

    fun setGenreId(genreId: Int) {
        if (genreIdFlow.value == genreId) return
        genreIdFlow.value = genreId
        _uiState.update { it.copy(genreId = genreId, isInitialized = true) }
    }
}

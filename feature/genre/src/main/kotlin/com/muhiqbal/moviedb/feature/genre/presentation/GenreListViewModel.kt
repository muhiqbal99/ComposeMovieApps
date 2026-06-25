package com.muhiqbal.moviedb.feature.genre.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhiqbal.moviedb.core.domain.repository.GenreRepository
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreListViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenreUiState>(GenreUiState.Loading)
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    init {
        loadGenres()
    }

    fun loadGenres() {
        _uiState.value = GenreUiState.Loading
        viewModelScope.launch {
            genreRepository.getGenres()
                .onSuccess { genres ->
                    if (genres.isEmpty()) {
                        _uiState.value = GenreUiState.Error(null)
                        return@onSuccess
                    }
                    val sections = genres.map { genre ->
                        async {
                            val movies = movieRepository.getMoviePreviewByGenre(genre.id)
                                .getOrDefault(emptyList())
                            GenreSection(genre = genre, movies = movies)
                        }
                    }.awaitAll()
                    _uiState.value = GenreUiState.Success(sections)
                }
                .onFailure { error ->
                    _uiState.value = GenreUiState.Error(error)
                }
        }
    }
}

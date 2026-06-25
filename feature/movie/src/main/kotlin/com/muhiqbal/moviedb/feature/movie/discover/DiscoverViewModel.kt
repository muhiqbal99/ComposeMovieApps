package com.muhiqbal.moviedb.feature.movie.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    movieRepository: MovieRepository,
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = movieRepository
        .getPopularMovies()
        .cachedIn(viewModelScope)
}

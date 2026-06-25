package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.muhiqbal.moviedb.feature.movie.list.MovieListRoute
import com.muhiqbal.moviedb.feature.movie.list.MovieListViewModel
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailRoute
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailViewModel

fun EntryProviderScope<NavKey>.movieGraph(
    navigator: MovieNavigator,
) {
    entry<MovieDestination.MovieList> { key ->
        val viewModel = hiltViewModel<MovieListViewModel>()
        LaunchedEffect(key.genreId) {
            viewModel.setGenreId(key.genreId)
        }
        MovieListRoute(
            genreName = key.genreName,
            viewModel = viewModel,
            onMovieClick = { movieId -> navigator.navigateToMovieDetail(movieId) },
            onBackClick = { navigator.navigateBack() },
        )
    }
    entry<MovieDestination.MovieDetail> { key ->
        val viewModel = hiltViewModel<MovieDetailViewModel>()
        LaunchedEffect(key.movieId) {
            viewModel.setMovieId(key.movieId)
        }
        MovieDetailRoute(
            viewModel = viewModel,
            onBackClick = { navigator.navigateBack() },
        )
    }
}

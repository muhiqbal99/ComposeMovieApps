package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailRoute
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailViewModel
import com.muhiqbal.moviedb.feature.movie.search.SearchRoute
import com.muhiqbal.moviedb.feature.movie.search.SearchViewModel

fun EntryProviderScope<NavKey>.searchGraph(
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit,
) {
    entry<SearchDestination.Search> {
        val viewModel = hiltViewModel<SearchViewModel>()
        SearchRoute(
            viewModel = viewModel,
            onMovieClick = onMovieClick,
        )
    }
    entry<SearchDestination.MovieDetail> { key ->
        val viewModel = hiltViewModel<MovieDetailViewModel>()
        LaunchedEffect(key.movieId) {
            viewModel.setMovieId(key.movieId)
        }
        MovieDetailRoute(
            viewModel = viewModel,
            onBackClick = onBack,
        )
    }
}

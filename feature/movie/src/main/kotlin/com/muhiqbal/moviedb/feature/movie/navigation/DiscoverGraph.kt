package com.muhiqbal.moviedb.feature.movie.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailRoute
import com.muhiqbal.moviedb.feature.movie.detail.MovieDetailViewModel
import com.muhiqbal.moviedb.feature.movie.discover.DiscoverRoute
import com.muhiqbal.moviedb.feature.movie.discover.DiscoverViewModel

fun EntryProviderScope<NavKey>.discoverGraph(
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit,
) {
    entry<DiscoverDestination.DiscoverList> {
        val viewModel = hiltViewModel<DiscoverViewModel>()
        DiscoverRoute(
            viewModel = viewModel,
            onMovieClick = onMovieClick,
        )
    }
    entry<DiscoverDestination.MovieDetail> { key ->
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

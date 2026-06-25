package com.muhiqbal.moviedb.feature.genre.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.muhiqbal.moviedb.feature.genre.presentation.GenreListRoute
import com.muhiqbal.moviedb.feature.genre.presentation.GenreListViewModel

fun EntryProviderScope<NavKey>.genreGraph(
    navigator: GenreNavigator,
) {
    entry<GenreDestination.GenreList> {
        val viewModel = hiltViewModel<GenreListViewModel>()
        GenreListRoute(
            viewModel = viewModel,
            onGenreClick = { genre -> navigator.navigateToMovieList(genre.id, genre.name) },
            onMovieClick = { movieId -> navigator.navigateToMovieDetail(movieId) },
        )
    }
}

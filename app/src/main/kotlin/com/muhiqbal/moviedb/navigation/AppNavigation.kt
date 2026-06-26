package com.muhiqbal.moviedb.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.muhiqbal.moviedb.feature.genre.navigation.GenreDestination
import com.muhiqbal.moviedb.feature.genre.navigation.GenreNavigator
import com.muhiqbal.moviedb.feature.genre.navigation.genreGraph
import com.muhiqbal.moviedb.feature.movie.navigation.DiscoverDestination
import com.muhiqbal.moviedb.feature.movie.navigation.MovieDestination
import com.muhiqbal.moviedb.feature.movie.navigation.MovieNavigator
import com.muhiqbal.moviedb.feature.movie.navigation.SearchDestination
import com.muhiqbal.moviedb.feature.movie.navigation.discoverGraph
import com.muhiqbal.moviedb.feature.movie.navigation.movieGraph
import com.muhiqbal.moviedb.feature.movie.navigation.searchGraph

private enum class Tab { Browse, Discover, Search }

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val activity = LocalActivity.current

    val browseBackStack = rememberNavBackStack(GenreDestination.GenreList as NavKey)
    val discoverBackStack = rememberNavBackStack(DiscoverDestination.DiscoverList as NavKey)
    val searchBackStack = rememberNavBackStack(SearchDestination.Search as NavKey)

    var selectedTab by rememberSaveable { mutableStateOf(Tab.Browse) }

    BackHandler {
        when (selectedTab) {
            Tab.Browse -> {
                if (browseBackStack.size > 1) {
                    browseBackStack.popToRoot()
                } else {
                    activity?.finish()
                }
            }

            Tab.Discover -> {
                if (discoverBackStack.size > 1) {
                    discoverBackStack.popToRoot()
                } else {
                    selectedTab = Tab.Browse
                }
            }

            Tab.Search -> {
                if (searchBackStack.size > 1) {
                    searchBackStack.popToRoot()
                } else {
                    selectedTab = Tab.Browse
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                NavigationBarItem(
                    selected = selectedTab == Tab.Browse,
                    onClick = {
                        if (selectedTab == Tab.Browse && browseBackStack.size > 1) browseBackStack.popToRoot()
                        selectedTab = Tab.Browse
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Browse") },
                    colors = navBarItemColors(),
                )

                NavigationBarItem(
                    selected = selectedTab == Tab.Discover,
                    onClick = { selectedTab = Tab.Discover },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Discover") },
                    colors = navBarItemColors(),
                )

                NavigationBarItem(
                    selected = selectedTab == Tab.Search,
                    onClick = { selectedTab = Tab.Search },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Search") },
                    colors = navBarItemColors(),
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            if (selectedTab == Tab.Browse) {
                NavDisplay(
                    backStack = browseBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    transitionSpec = { simpleFade() },
                    popTransitionSpec = { simpleFade() },
                    predictivePopTransitionSpec = { simpleFade() },
                    entryProvider = entryProvider {
                        genreGraph(
                            navigator = object : GenreNavigator {
                                override fun navigateToMovieList(
                                    genreId: Int,
                                    genreName: String,
                                ) {
                                    browseBackStack.add(
                                        MovieDestination.MovieList(
                                            genreId = genreId,
                                            genreName = genreName,
                                        )
                                    )
                                }

                                override fun navigateToMovieDetail(movieId: Int) {
                                    browseBackStack.add(
                                        MovieDestination.MovieDetail(movieId)
                                    )
                                }
                            }
                        )

                        movieGraph(
                            navigator = object : MovieNavigator {
                                override fun navigateToMovieDetail(movieId: Int) {
                                    browseBackStack.add(
                                        MovieDestination.MovieDetail(movieId)
                                    )
                                }

                                override fun navigateBack() {
                                    browseBackStack.popToRoot()
                                }
                            }
                        )
                    },
                    onBack = { browseBackStack.popToRoot() },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            if (selectedTab == Tab.Discover) {
                NavDisplay(
                    backStack = discoverBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    transitionSpec = { simpleFade() },
                    popTransitionSpec = { simpleFade() },
                    predictivePopTransitionSpec = { simpleFade() },
                    entryProvider = entryProvider {
                        discoverGraph(
                            onMovieClick = { movieId ->
                                discoverBackStack.add(
                                    DiscoverDestination.MovieDetail(movieId)
                                )
                            },
                            onBack = { discoverBackStack.popToRoot() },
                        )
                    },
                    onBack = { discoverBackStack.popToRoot() },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            if (selectedTab == Tab.Search) {
                NavDisplay(
                    backStack = searchBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    transitionSpec = { simpleFade() },
                    popTransitionSpec = { simpleFade() },
                    predictivePopTransitionSpec = { simpleFade() },
                    entryProvider = entryProvider {
                        searchGraph(
                            onMovieClick = { movieId ->
                                searchBackStack.add(
                                    SearchDestination.MovieDetail(movieId)
                                )
                            },
                            onBack = { searchBackStack.popToRoot() },
                        )
                    },
                    onBack = { searchBackStack.popToRoot() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

fun <T> MutableList<T>.popToRoot() {
    if (size > 1) removeLastOrNull()
}

private fun simpleFade(): ContentTransform =
    fadeIn(tween(durationMillis = 180)) togetherWith fadeOut(tween(durationMillis = 180))

@Composable
private fun navBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

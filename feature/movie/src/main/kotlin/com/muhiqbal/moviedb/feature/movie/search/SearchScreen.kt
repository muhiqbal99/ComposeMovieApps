package com.muhiqbal.moviedb.feature.movie.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.ui.error.errorMessageFor
import com.muhiqbal.moviedb.feature.movie.R
import com.muhiqbal.moviedb.feature.movie.list.AppendErrorItem
import com.muhiqbal.moviedb.feature.movie.list.MovieErrorContent
import com.muhiqbal.moviedb.feature.movie.list.MovieItem
import com.muhiqbal.moviedb.feature.movie.list.MovieListShimmer

@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val results = viewModel.results.collectAsLazyPagingItems()
    SearchScreen(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        results = results,
        onMovieClick = onMovieClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    results: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feature_search_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { heading() },
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text(stringResource(R.string.feature_search_hint)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    query.isBlank() -> {
                        Text(
                            text = stringResource(R.string.feature_search_empty_prompt),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                        )
                    }
                    results.loadState.refresh is LoadState.Loading -> {
                        MovieListShimmer()
                    }
                    results.loadState.refresh is LoadState.Error -> {
                        val error = (results.loadState.refresh as LoadState.Error).error
                        MovieErrorContent(
                            message = errorMessageFor(error),
                            onRetry = { results.retry() },
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    results.itemCount == 0 -> {
                        Text(
                            text = stringResource(R.string.feature_search_no_results, query),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        ) {
                            items(
                                count = results.itemCount,
                                key = results.itemKey { it.id },
                            ) { index ->
                                val movie = results[index]
                                if (movie != null) {
                                    MovieItem(
                                        movie = movie,
                                        onClick = { onMovieClick(movie.id) },
                                        modifier = Modifier.padding(bottom = 12.dp),
                                    )
                                }
                            }
                            when (val appendState = results.loadState.append) {
                                is LoadState.Loading -> item {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp),
                                            strokeWidth = 3.dp,
                                        )
                                    }
                                }
                                is LoadState.Error -> item {
                                    AppendErrorItem(
                                        message = errorMessageFor(appendState.error),
                                        onRetry = { results.retry() },
                                    )
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}

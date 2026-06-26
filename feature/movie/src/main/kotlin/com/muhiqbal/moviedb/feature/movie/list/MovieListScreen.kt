package com.muhiqbal.moviedb.feature.movie.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.ui.component.ShimmerBox
import com.muhiqbal.moviedb.core.ui.error.errorMessageFor
import com.muhiqbal.moviedb.core.ui.theme.RatingGold
import com.muhiqbal.moviedb.core.ui.util.TmdbImageConfig
import com.muhiqbal.moviedb.feature.movie.R
import java.util.Locale

@Composable
fun MovieListRoute(
    genreName: String,
    viewModel: MovieListViewModel,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()
    MovieListScreen(
        genreName = genreName,
        movies = movies,
        onMovieClick = onMovieClick,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    genreName: String,
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = genreName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { heading() },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.feature_movie_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    MovieListShimmer()
                }
                movies.loadState.refresh is LoadState.Error -> {
                    val error = (movies.loadState.refresh as LoadState.Error).error
                    MovieErrorContent(
                        message = errorMessageFor(error),
                        onRetry = { movies.retry() },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    MoviePagingList(
                        movies = movies,
                        onMovieClick = onMovieClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviePagingList(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id },
        ) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieItem(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
        }

        when (val appendState = movies.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            is LoadState.Error -> {
                item {
                    AppendErrorItem(
                        message = errorMessageFor(appendState.error),
                        onRetry = { movies.retry() },
                    )
                }
            }
            else -> Unit
        }
    }
}

@Composable
internal fun MovieItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 135.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                AsyncImage(
                    model = movie.posterPath?.let { TmdbImageConfig.BASE_W500 + it },
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (movie.releaseDate.length >= 4) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = movie.releaseDate.take(4),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                )
            }
        }
    }
}

@Composable
internal fun MovieListShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(6) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier.size(width = 90.dp, height = 135.dp),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ShimmerBox(
                        modifier = Modifier.fillMaxWidth(0.8f).height(18.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier.fillMaxWidth(0.4f).height(14.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier.fillMaxWidth().height(12.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier.fillMaxWidth(0.6f).height(12.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun MovieErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(24.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.feature_movie_retry),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
internal fun AppendErrorItem(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onRetry) {
            Text(stringResource(R.string.feature_movie_retry))
        }
    }
}



private val previewMovies = listOf(
    Movie(
        id = 1,
        title = "Avengers: Endgame",
        overview = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more.",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.4,
        releaseDate = "2019-04-26",
        genreIds = listOf(28, 12),
    ),
    Movie(
        id = 2,
        title = "Inception",
        overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.8,
        releaseDate = "2010-07-16",
        genreIds = listOf(28, 878),
    ),
)

@Preview(showBackground = true)
@Composable
private fun MovieItemPreview() {
    MaterialTheme {
        MovieItem(
            movie = previewMovies[0],
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListPreview() {
    MaterialTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(previewMovies) { movie ->
                MovieItem(movie = movie, onClick = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieErrorContentPreview() {
    MaterialTheme {
        MovieErrorContent(
            message = "Failed to load movies. Please check your connection.",
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppendErrorItemPreview() {
    MaterialTheme {
        AppendErrorItem(
            message = "Error loading more movies",
            onRetry = {},
        )
    }
}

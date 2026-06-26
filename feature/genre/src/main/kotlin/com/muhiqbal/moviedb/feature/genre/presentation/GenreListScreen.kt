package com.muhiqbal.moviedb.feature.genre.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.ui.component.ShimmerBox
import com.muhiqbal.moviedb.core.ui.error.errorMessageFor
import com.muhiqbal.moviedb.core.ui.theme.RatingGold
import com.muhiqbal.moviedb.core.ui.util.TmdbImageConfig
import com.muhiqbal.moviedb.feature.genre.R
import java.util.Locale

@Composable
fun GenreListRoute(
    viewModel: GenreListViewModel,
    onGenreClick: (Genre) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GenreListScreen(
        uiState = uiState,
        onGenreClick = onGenreClick,
        onMovieClick = onMovieClick,
        onRetry = viewModel::loadGenres,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreListScreen(
    uiState: GenreUiState,
    onGenreClick: (Genre) -> Unit,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.genre_browse_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = stringResource(R.string.genre_browse_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (uiState) {
                is GenreUiState.Loading -> {
                    GenreLoadingShimmer()
                }

                is GenreUiState.Error -> {
                    GenreErrorContent(
                        message = errorMessageFor(uiState.error),
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is GenreUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(items = uiState.sections, key = { it.genre.id }) { section ->
                            GenreCarouselSection(
                                section = section,
                                onSeeMoreClick = { onGenreClick(section.genre) },
                                onMovieClick = onMovieClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreCarouselSection(
    section: GenreSection,
    onSeeMoreClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 4.dp),
        ) {
            Text(
                text = section.genre.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onSeeMoreClick) {
                Text(
                    text = stringResource(R.string.genre_see_more),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        if (section.movies.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Text(
                    text = stringResource(R.string.genre_no_movies),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(items = section.movies, key = { it.id }) { movie ->
                    MovieCarouselCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieCarouselCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = movie.posterPath?.let { TmdbImageConfig.BASE_W342 + it },
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.80f)),
                    ),
                )
                .padding(horizontal = 6.dp, vertical = 6.dp),
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(10.dp),
                    )
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun GenreLoadingShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
    ) {
        repeat(4) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                ShimmerBox(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, bottom = 12.dp)
                        .width(140.dp)
                        .height(18.dp),
                    shape = RoundedCornerShape(4.dp),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    repeat(3) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(120.dp)
                                .aspectRatio(2f / 3f),
                            shape = RoundedCornerShape(10.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreErrorContent(
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
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.genre_retry),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreListScreenSuccessPreview() {
    MaterialTheme {
        val fakeMovies = listOf(
            Movie(
                id = 1,
                title = "Avengers",
                overview = "",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.4,
                releaseDate = "2019-04-26",
                genreIds = listOf(28)
            ),
            Movie(
                id = 2,
                title = "Iron Man",
                overview = "",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.9,
                releaseDate = "2008-05-02",
                genreIds = listOf(28)
            ),
        )
        GenreListScreen(
            uiState = GenreUiState.Success(
                sections = listOf(
                    GenreSection(genre = Genre(id = 28, name = "Action"), movies = fakeMovies),
                    GenreSection(genre = Genre(id = 35, name = "Comedy"), movies = fakeMovies),
                    GenreSection(genre = Genre(id = 18, name = "Drama"), movies = emptyList()),
                ),
            ),
            onGenreClick = {},
            onMovieClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreListScreenLoadingPreview() {
    MaterialTheme {
        GenreListScreen(
            uiState = GenreUiState.Loading,
            onGenreClick = {},
            onMovieClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreListScreenErrorPreview() {
    MaterialTheme {
        GenreListScreen(
            uiState = GenreUiState.Error(null),
            onGenreClick = {},
            onMovieClick = {},
            onRetry = {},
        )
    }
}

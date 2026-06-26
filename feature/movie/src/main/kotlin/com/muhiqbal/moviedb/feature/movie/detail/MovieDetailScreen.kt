package com.muhiqbal.moviedb.feature.movie.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.model.Video
import com.muhiqbal.moviedb.core.ui.component.ShimmerBox
import com.muhiqbal.moviedb.core.ui.error.errorMessageFor
import com.muhiqbal.moviedb.core.ui.theme.RatingGold
import com.muhiqbal.moviedb.core.ui.util.TmdbImageConfig
import com.muhiqbal.moviedb.feature.movie.R
import com.muhiqbal.moviedb.feature.movie.detail.component.ReviewItem
import com.muhiqbal.moviedb.feature.movie.detail.component.TrailerSection
import java.util.Locale

@Composable
fun MovieDetailRoute(
    viewModel: MovieDetailViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    MovieDetailScreen(
        uiState = uiState,
        reviews = reviews,
        onBackClick = onBackClick,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    reviews: LazyPagingItems<Review>,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is MovieDetailUiState.Loading -> {
                MovieDetailShimmer()
            }

            is MovieDetailUiState.Error -> {
                MovieDetailErrorContent(
                    message = errorMessageFor(uiState.error),
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            is MovieDetailUiState.Success -> {
                MovieDetailContent(
                    movie = uiState.movie,
                    trailer = uiState.trailer,
                    cast = uiState.cast,
                    reviews = reviews,
                )
            }
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .size(40.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.45f),
                    shape = CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.feature_movie_back),
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    trailer: Video?,
    cast: List<Cast>,
    reviews: LazyPagingItems<Review>,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        item(key = "hero") {
            HeroSection(movie = movie, hasTrailer = trailer != null)
        }

        item(key = "info") {
            MovieInfoSection(movie = movie)
        }

        item(key = "tabs") {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = stringResource(R.string.feature_movie_detail_tab),
                            fontWeight = if (selectedTab == 0) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    },
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = stringResource(R.string.feature_movie_reviews),
                            fontWeight = if (selectedTab == 1) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    },
                )
            }
        }

        if (selectedTab == 0) {
            if (movie.genres.isNotEmpty()) {
                item(key = "genres") {
                    GenreChipsRow(genres = movie.genres)
                }
            }

            item(key = "overview") {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    SectionTitle(text = stringResource(R.string.feature_movie_overview))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                    )
                }
            }

            if (trailer != null && trailer.key.isNotBlank()) {
                item(key = "trailer") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        SectionTitle(text = stringResource(R.string.feature_movie_trailer))
                        Spacer(modifier = Modifier.height(8.dp))
                        TrailerSection(videoKey = trailer.key)
                    }
                }
            }

            if (cast.isNotEmpty()) {
                item(key = "cast_header") {
                    SectionTitle(
                        text = stringResource(R.string.feature_movie_cast_crew),
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 0.dp
                        ),
                    )
                }
                item(key = "cast_list") {
                    CastSection(cast = cast)
                }
            }
        } else {
            item(key = "reviews_summary") {
                ReviewsSummaryHeader(
                    rating = movie.voteAverage,
                    voteCount = movie.voteCount,
                )
            }

            when (val refreshState = reviews.loadState.refresh) {
                is LoadState.Loading -> {
                    if (reviews.itemCount == 0) {
                        item(key = "reviews_loading") {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                }

                is LoadState.Error -> {
                    item(key = "reviews_refresh_error") {
                        ReviewsErrorItem(
                            message = errorMessageFor(refreshState.error),
                            onRetry = { reviews.retry() },
                        )
                    }
                }

                else -> Unit
            }

            if (reviews.itemCount == 0 && reviews.loadState.refresh !is LoadState.Loading) {
                item(key = "no_reviews") {
                    Text(
                        text = stringResource(R.string.feature_movie_no_reviews),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    )
                }
            }

            items(
                count = reviews.itemCount,
                key = reviews.itemKey { it.id },
            ) { index ->
                val review = reviews[index]
                if (review != null) {
                    ReviewItem(
                        review = review,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }
            }

            when (val appendState = reviews.loadState.append) {
                is LoadState.Loading -> {
                    item(key = "reviews_append_loading") {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }

                is LoadState.Error -> {
                    item(key = "reviews_append_error") {
                        ReviewsErrorItem(
                            message = errorMessageFor(appendState.error),
                            onRetry = { reviews.retry() },
                        )
                    }
                }

                else -> Unit
            }
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeroSection(
    movie: MovieDetail,
    hasTrailer: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
    ) {
        AsyncImage(
            model = movie.backdropPath?.let { TmdbImageConfig.BASE_W780 + it }
                ?: movie.posterPath?.let { TmdbImageConfig.BASE_W342 + it },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.55f),
                            MaterialTheme.colorScheme.background,
                        ),
                        startY = 60f,
                    )
                )
        )

        if (hasTrailer) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Center)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 8.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 16.dp, y = 48.dp)
                .size(width = 95.dp, height = 142.dp),
        ) {
            AsyncImage(
                model = movie.posterPath?.let { TmdbImageConfig.BASE_W342 + it },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
internal fun MovieInfoSection(
    movie: MovieDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 126.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { i ->
                val filled = i < (movie.voteAverage / 2).toInt()
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (filled) RatingGold else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(14.dp),
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage / 2),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        val meta = buildList {
            movie.runtime?.takeIf { it > 0 }?.let { add("${it / 60}hr ${it % 60}m") }
            if (movie.releaseDate.length >= 4) add(movie.releaseDate.take(4))
        }.joinToString(" · ")
        if (meta.isNotEmpty()) {
            Text(
                text = meta,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    Spacer(modifier = Modifier.height(42.dp))
}

@Composable
private fun GenreChipsRow(
    genres: List<Genre>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items(genres, key = { it.id }) { genre ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
            ) {
                Text(
                    text = genre.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

@Composable
private fun CastSection(
    cast: List<Cast>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        items(cast, key = { it.id }) { person ->
            CastMemberItem(cast = person)
        }
    }
}

@Composable
private fun CastMemberItem(
    cast: Cast,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(72.dp),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            if (cast.profilePath != null) {
                AsyncImage(
                    model = TmdbImageConfig.BASE_W342 + cast.profilePath,
                    contentDescription = cast.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Center),
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = cast.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (cast.character.isNotBlank()) {
            Text(
                text = cast.character,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ReviewsSummaryHeader(
    rating: Double,
    voteCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = String.format(Locale.getDefault(), "%.1f", rating / 2),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row {
                repeat(5) { i ->
                    val filled = i < (rating / 2).toInt()
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (filled) RatingGold else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.feature_movie_votes, voteCount),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReviewsErrorItem(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRetry) { Text(stringResource(R.string.feature_movie_retry)) }
    }
}

@Composable
private fun MovieDetailShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        ShimmerBox(modifier = Modifier.fillMaxWidth().height(280.dp))
        Row(modifier = Modifier.padding(16.dp)) {
            ShimmerBox(
                modifier = Modifier.size(width = 95.dp, height = 142.dp),
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.7f).height(22.dp),
                    shape = RoundedCornerShape(4.dp),
                )
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.4f).height(14.dp),
                    shape = RoundedCornerShape(4.dp),
                )
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.5f).height(14.dp),
                    shape = RoundedCornerShape(4.dp),
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            repeat(4) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth().height(12.dp),
                    shape = RoundedCornerShape(4.dp),
                )
            }
        }
    }
}

@Composable
private fun MovieDetailErrorContent(
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
            textAlign = TextAlign.Center,
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


private val previewMovieDetail = MovieDetail(
    id = 1,
    title = "John Wick: Parabellum",
    overview = "Super-assassin John Wick returns with a \$14 million price tag on his head and an army of bounty-hunting killers on his trail.",
    posterPath = null,
    backdropPath = null,
    voteAverage = 7.4,
    voteCount = 9188,
    releaseDate = "2019-05-17",
    runtime = 131,
    genres = listOf(Genre(id = 28, name = "Action"), Genre(id = 53, name = "Thriller")),
    status = "Released",
)

@Preview(showBackground = true)
@Composable
private fun MovieInfoSectionPreview() {
    MaterialTheme {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            MovieInfoSection(movie = previewMovieDetail)
        }
    }
}

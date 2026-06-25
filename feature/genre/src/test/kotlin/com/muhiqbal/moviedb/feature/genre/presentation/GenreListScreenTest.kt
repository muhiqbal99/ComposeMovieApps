package com.muhiqbal.moviedb.feature.genre.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.model.Movie
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class GenreListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val movies = listOf(
        Movie(
            id = 11,
            title = "Avengers",
            overview = "",
            posterPath = null,
            backdropPath = null,
            voteAverage = 8.4,
            releaseDate = "2019-04-26",
            genreIds = listOf(28),
        ),
    )

    private val successState = GenreUiState.Success(
        sections = listOf(
            GenreSection(genre = Genre(id = 28, name = "Action"), movies = movies),
            GenreSection(genre = Genre(id = 35, name = "Comedy"), movies = movies),
        ),
    )

    @Test
    fun rendersGenreSectionsAndMovies() {
        composeRule.setContent {
            GenreListScreen(
                uiState = successState,
                onGenreClick = {},
                onMovieClick = {},
                onRetry = {},
            )
        }

        composeRule.onNodeWithText("Action").assertIsDisplayed()
        composeRule.onNodeWithText("Comedy").assertIsDisplayed()
        composeRule.onAllNodesWithText("Avengers")[0].assertIsDisplayed()
    }

    @Test
    fun seeMoreButtonTriggersGenreClick() {
        var clickedGenre: Genre? = null
        composeRule.setContent {
            GenreListScreen(
                uiState = successState,
                onGenreClick = { clickedGenre = it },
                onMovieClick = {},
                onRetry = {},
            )
        }

        composeRule.onAllNodesWithText("See More")[0].performClick()

        assertThat(clickedGenre).isEqualTo(Genre(id = 28, name = "Action"))
    }

    @Test
    fun moviePosterTriggersMovieClick() {
        var clickedMovieId: Int? = null
        composeRule.setContent {
            GenreListScreen(
                uiState = successState,
                onGenreClick = {},
                onMovieClick = { clickedMovieId = it },
                onRetry = {},
            )
        }

        composeRule.onAllNodesWithText("Avengers")[0].performClick()

        assertThat(clickedMovieId).isEqualTo(11)
    }
}

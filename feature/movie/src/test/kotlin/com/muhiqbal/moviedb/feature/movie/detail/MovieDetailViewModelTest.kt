package com.muhiqbal.moviedb.feature.movie.detail

import com.google.common.truth.Truth.assertThat
import com.muhiqbal.moviedb.core.domain.model.Video
import com.muhiqbal.moviedb.core.testing.fake.FakeMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeMovieRepository()
        viewModel = MovieDetailViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        assertThat(viewModel.uiState.value).isInstanceOf(MovieDetailUiState.Loading::class.java)
    }

    @Test
    fun `setMovieId loads detail and shows Success`() = runTest {
        viewModel.setMovieId(1)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(MovieDetailUiState.Success::class.java)
        val success = state as MovieDetailUiState.Success
        assertThat(success.movie.title).isEqualTo("Movie 1")
    }

    @Test
    fun `setMovieId includes YouTube trailer when available`() = runTest {
        viewModel.setMovieId(1)
        advanceUntilIdle()
        val state = viewModel.uiState.value as MovieDetailUiState.Success
        assertThat(state.trailer).isNotNull()
        assertThat(state.trailer!!.site).isEqualTo("YouTube")
        assertThat(state.trailer.type).isEqualTo("Trailer")
    }

    @Test
    fun `setMovieId returns null trailer when no YouTube video`() = runTest {
        fakeRepository.videosResult = Result.success(
            listOf(Video(id = "v1", key = "abc", name = "Clip", site = "Vimeo", type = "Clip"))
        )
        viewModel.setMovieId(1)
        advanceUntilIdle()
        val state = viewModel.uiState.value as MovieDetailUiState.Success
        assertThat(state.trailer).isNull()
    }

    @Test
    fun `setMovieId shows Error when detail fails`() = runTest {
        fakeRepository.movieDetailResult = Result.failure(Exception("Not found"))
        viewModel.setMovieId(1)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(MovieDetailUiState.Error::class.java)
        val error = viewModel.uiState.value as MovieDetailUiState.Error
        assertThat(error.error?.message).isEqualTo("Not found")
    }

    @Test
    fun `retry reloads the movie detail`() = runTest {
        viewModel.setMovieId(1)
        advanceUntilIdle()
        fakeRepository.movieDetailResult = Result.failure(Exception("Server error"))
        viewModel.retry()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(MovieDetailUiState.Error::class.java)
    }

    @Test
    fun `setMovieId does not reload when same id`() = runTest {
        viewModel.setMovieId(1)
        advanceUntilIdle()
        val firstState = viewModel.uiState.value
        viewModel.setMovieId(1)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isEqualTo(firstState)
    }
}

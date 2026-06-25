package com.muhiqbal.moviedb.feature.genre.presentation

import com.google.common.truth.Truth.assertThat
import com.muhiqbal.moviedb.core.testing.fake.FakeGenreRepository
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
class GenreListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeGenreRepository: FakeGenreRepository
    private lateinit var fakeMovieRepository: FakeMovieRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeGenreRepository = FakeGenreRepository()
        fakeMovieRepository = FakeMovieRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        GenreListViewModel(fakeGenreRepository, fakeMovieRepository)

    @Test
    fun `initial state is Loading`() {
        val viewModel = createViewModel()
        assertThat(viewModel.uiState.value).isInstanceOf(GenreUiState.Loading::class.java)
    }

    @Test
    fun `uiState is Success when genres loaded successfully`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(GenreUiState.Success::class.java)
        val success = state as GenreUiState.Success
        assertThat(success.sections).hasSize(3)
        assertThat(success.sections.first().genre.name).isEqualTo("Action")
    }

    @Test
    fun `sections carry movie previews from movie repository`() = runTest(testDispatcher) {
        fakeMovieRepository.moviePreviewResult = Result.success(fakeMovieRepository.moviesResult)
        val viewModel = createViewModel()
        advanceUntilIdle()
        val success = viewModel.uiState.value as GenreUiState.Success
        assertThat(success.sections.first().movies).hasSize(2)
    }

    @Test
    fun `uiState is Error when repository fails`() = runTest(testDispatcher) {
        fakeGenreRepository.genresResult = Result.failure(Exception("Network error"))
        val viewModel = createViewModel()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(GenreUiState.Error::class.java)
        val error = state as GenreUiState.Error
        assertThat(error.error?.message).isEqualTo("Network error")
    }

    @Test
    fun `loadGenres resets to Loading before fetching`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.loadGenres()
        assertThat(viewModel.uiState.value).isInstanceOf(GenreUiState.Loading::class.java)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(GenreUiState.Success::class.java)
    }

    @Test
    fun `uiState is Error when genres list is empty`() = runTest(testDispatcher) {
        fakeGenreRepository.genresResult = Result.success(emptyList())
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(GenreUiState.Error::class.java)
    }

    @Test
    fun `getGenres is called once on init`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()
        assertThat(fakeGenreRepository.getGenresCallCount).isEqualTo(1)
    }

    @Test
    fun `loadGenres calls getGenres again on retry`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.loadGenres()
        advanceUntilIdle()
        assertThat(fakeGenreRepository.getGenresCallCount).isEqualTo(2)
    }
}

package com.muhiqbal.moviedb.feature.movie.list

import com.google.common.truth.Truth.assertThat
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
class MovieListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeMovieRepository()
        viewModel = MovieListViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has no genre set`() {
        assertThat(viewModel.uiState.value.genreId).isEqualTo(-1)
        assertThat(viewModel.uiState.value.isInitialized).isFalse()
    }

    @Test
    fun `setGenreId updates state`() = runTest {
        viewModel.setGenreId(28)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.genreId).isEqualTo(28)
        assertThat(viewModel.uiState.value.isInitialized).isTrue()
    }

    @Test
    fun `setGenreId does not update when same id`() = runTest {
        viewModel.setGenreId(28)
        advanceUntilIdle()
        val stateAfterFirst = viewModel.uiState.value
        viewModel.setGenreId(28)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value).isEqualTo(stateAfterFirst)
    }
}

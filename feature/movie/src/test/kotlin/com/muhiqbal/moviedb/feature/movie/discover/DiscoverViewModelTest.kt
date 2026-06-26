package com.muhiqbal.moviedb.feature.movie.discover

import com.google.common.truth.Truth.assertThat
import com.muhiqbal.moviedb.core.testing.fake.FakeMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: DiscoverViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeMovieRepository()
        viewModel = DiscoverViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `movies exposes a paging stream of popular movies`() = runTest {
        assertThat(viewModel.movies.first()).isNotNull()
    }
}

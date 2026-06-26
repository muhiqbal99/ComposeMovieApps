package com.muhiqbal.moviedb.feature.movie.search

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
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeMovieRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeMovieRepository()
        viewModel = SearchViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial query is blank`() {
        assertThat(viewModel.query.value).isEmpty()
    }

    @Test
    fun `blank query produces empty results`() = runTest {
        assertThat(viewModel.results.first()).isNotNull()
    }

    @Test
    fun `non-blank query searches movies`() = runTest {
        viewModel.onQueryChange("Movie")
        assertThat(viewModel.results.first()).isNotNull()
    }

    @Test
    fun `onQueryChange updates query state`() {
        viewModel.onQueryChange("batman")
        assertThat(viewModel.query.value).isEqualTo("batman")
    }
}

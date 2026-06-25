package com.muhiqbal.moviedb.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.muhiqbal.moviedb.core.common.result.NoConnectivityException
import com.muhiqbal.moviedb.core.network.TmdbApiService
import com.muhiqbal.moviedb.core.network.dto.MovieDto
import com.muhiqbal.moviedb.core.network.dto.PagedMoviesResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class MovieRepositoryImplTest {

    private val apiService = mockk<TmdbApiService>()
    private val repository = MovieRepositoryImpl(apiService)

    @Test
    fun `getMoviePreviewByGenre returns mapped movies limited to ten`() = runTest {
        val dtos = (1..15).map { MovieDto(id = it, title = "Movie $it") }
        coEvery { apiService.discoverMovies(genreId = 28, page = 1) } returns
            PagedMoviesResponseDto(page = 1, results = dtos, totalPages = 1)

        val result = repository.getMoviePreviewByGenre(28)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).hasSize(10)
        assertThat(result.getOrThrow().first().title).isEqualTo("Movie 1")
    }

    @Test
    fun `getMoviePreviewByGenre maps connectivity failure to NoConnectivityException`() = runTest {
        coEvery { apiService.discoverMovies(genreId = 28, page = 1) } throws
            UnknownHostException("Unable to resolve host")

        val result = repository.getMoviePreviewByGenre(28)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoConnectivityException::class.java)
    }

    @Test
    fun `getMovieDetail maps connectivity failure to NoConnectivityException`() = runTest {
        coEvery { apiService.getMovieDetail(1) } throws UnknownHostException("offline")

        val result = repository.getMovieDetail(1)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoConnectivityException::class.java)
    }
}

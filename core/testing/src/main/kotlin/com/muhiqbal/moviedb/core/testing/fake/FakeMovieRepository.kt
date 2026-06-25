package com.muhiqbal.moviedb.core.testing.fake

import androidx.paging.PagingData
import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.model.Video
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepository : MovieRepository {

    var moviesResult: List<Movie> = listOf(
        Movie(id = 1, title = "Movie 1", overview = "Overview 1", posterPath = null, backdropPath = null, voteAverage = 7.5, releaseDate = "2024-01-01", genreIds = listOf(28)),
        Movie(id = 2, title = "Movie 2", overview = "Overview 2", posterPath = null, backdropPath = null, voteAverage = 8.0, releaseDate = "2024-02-01", genreIds = listOf(28)),
    )
    var movieDetailResult: Result<MovieDetail> = Result.success(
        MovieDetail(id = 1, title = "Movie 1", overview = "Overview", posterPath = null, backdropPath = null, voteAverage = 7.5, voteCount = 100, releaseDate = "2024-01-01", runtime = 120, genres = emptyList(), status = "Released")
    )
    var reviewsResult: List<Review> = listOf(
        Review(id = "r1", author = "Author 1", content = "Great movie!", createdAt = "2024-01-01", rating = 8.0),
    )
    var videosResult: Result<List<Video>> = Result.success(
        listOf(Video(id = "v1", key = "dQw4w9WgXcQ", name = "Trailer", site = "YouTube", type = "Trailer"))
    )
    var creditsResult: Result<List<Cast>> = Result.success(emptyList())
    var moviePreviewResult: Result<List<Movie>> = Result.success(emptyList())

    override fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> =
        flowOf(PagingData.from(moviesResult))

    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        flowOf(PagingData.from(moviesResult))

    override fun searchMovies(query: String): Flow<PagingData<Movie>> =
        flowOf(PagingData.from(moviesResult.filter { it.title.contains(query, ignoreCase = true) }))

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> = movieDetailResult

    override fun getReviews(movieId: Int): Flow<PagingData<Review>> =
        flowOf(PagingData.from(reviewsResult))

    override suspend fun getVideos(movieId: Int): Result<List<Video>> = videosResult

    override suspend fun getCredits(movieId: Int): Result<List<Cast>> = creditsResult

    override suspend fun getMoviePreviewByGenre(genreId: Int): Result<List<Movie>> = moviePreviewResult
}

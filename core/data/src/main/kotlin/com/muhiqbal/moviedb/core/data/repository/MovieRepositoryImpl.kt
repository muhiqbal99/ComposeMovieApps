package com.muhiqbal.moviedb.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.muhiqbal.moviedb.core.common.result.toAppException
import com.muhiqbal.moviedb.core.data.mapper.toDomain
import com.muhiqbal.moviedb.core.data.paging.MoviePagingSource
import com.muhiqbal.moviedb.core.data.paging.PopularMoviesPagingSource
import com.muhiqbal.moviedb.core.data.paging.ReviewPagingSource
import com.muhiqbal.moviedb.core.data.paging.SearchMoviesPagingSource
import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.model.Video
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import com.muhiqbal.moviedb.core.network.TmdbApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
) : MovieRepository {

    override fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(apiService, genreId) },
        ).flow

    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { PopularMoviesPagingSource(apiService) },
        ).flow

    override fun searchMovies(query: String): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { SearchMoviesPagingSource(apiService, query) },
        ).flow

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> =
        try {
            val detail = apiService.getMovieDetail(movieId).toDomain()
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e.toAppException())
        }

    override fun getReviews(movieId: Int): Flow<PagingData<Review>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { ReviewPagingSource(apiService, movieId) },
        ).flow

    override suspend fun getVideos(movieId: Int): Result<List<Video>> =
        try {
            val videos = apiService.getMovieVideos(movieId).results
                ?.map { it.toDomain() }
                .orEmpty()
            Result.success(videos)
        } catch (e: Exception) {
            Result.failure(e.toAppException())
        }

    override suspend fun getCredits(movieId: Int): Result<List<Cast>> =
        try {
            val cast = apiService.getMovieCredits(movieId).cast
                ?.map { it.toDomain() }
                .orEmpty()
            Result.success(cast)
        } catch (e: Exception) {
            Result.failure(e.toAppException())
        }

    override suspend fun getMoviePreviewByGenre(genreId: Int): Result<List<Movie>> =
        try {
            val movies = apiService.discoverMovies(genreId = genreId, page = 1)
                .results
                ?.take(10)
                ?.map { it.toDomain() }
                .orEmpty()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e.toAppException())
        }

    companion object {
        private const val PAGE_SIZE = 20
    }
}

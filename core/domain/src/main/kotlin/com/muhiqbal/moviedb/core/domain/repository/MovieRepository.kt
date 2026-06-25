package com.muhiqbal.moviedb.core.domain.repository

import androidx.paging.PagingData
import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>>
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>
    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>
    fun getReviews(movieId: Int): Flow<PagingData<Review>>
    suspend fun getVideos(movieId: Int): Result<List<Video>>
    suspend fun getCredits(movieId: Int): Result<List<Cast>>
    suspend fun getMoviePreviewByGenre(genreId: Int): Result<List<Movie>>
}

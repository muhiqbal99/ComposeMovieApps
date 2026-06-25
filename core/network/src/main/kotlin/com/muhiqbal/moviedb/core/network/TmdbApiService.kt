package com.muhiqbal.moviedb.core.network

import com.muhiqbal.moviedb.core.network.dto.CreditsResponseDto
import com.muhiqbal.moviedb.core.network.dto.GenreListResponseDto
import com.muhiqbal.moviedb.core.network.dto.MovieDetailDto
import com.muhiqbal.moviedb.core.network.dto.PagedMoviesResponseDto
import com.muhiqbal.moviedb.core.network.dto.PagedReviewsResponseDto
import com.muhiqbal.moviedb.core.network.dto.VideosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreListResponseDto

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
    ): PagedMoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
    ): MovieDetailDto

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
    ): PagedReviewsResponseDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): VideosResponseDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
    ): CreditsResponseDto

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
    ): PagedMoviesResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
    ): PagedMoviesResponseDto
}

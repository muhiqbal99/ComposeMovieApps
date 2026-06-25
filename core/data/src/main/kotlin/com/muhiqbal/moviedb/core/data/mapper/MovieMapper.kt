package com.muhiqbal.moviedb.core.data.mapper

import com.muhiqbal.moviedb.core.domain.model.Cast
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.domain.model.MovieDetail
import com.muhiqbal.moviedb.core.domain.model.Review
import com.muhiqbal.moviedb.core.domain.model.Video
import com.muhiqbal.moviedb.core.network.dto.CastDto
import com.muhiqbal.moviedb.core.network.dto.GenreDto
import com.muhiqbal.moviedb.core.network.dto.MovieDetailDto
import com.muhiqbal.moviedb.core.network.dto.MovieDto
import com.muhiqbal.moviedb.core.network.dto.ReviewDto
import com.muhiqbal.moviedb.core.network.dto.VideoDto

fun GenreDto.toDomain() = Genre(
    id = id ?: 0,
    name = name.orEmpty(),
)

fun MovieDto.toDomain() = Movie(
    id = id ?: 0,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage ?: 0.0,
    releaseDate = releaseDate.orEmpty(),
    genreIds = genreIds.orEmpty(),
)

fun MovieDetailDto.toDomain() = MovieDetail(
    id = id ?: 0,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    releaseDate = releaseDate.orEmpty(),
    runtime = runtime,
    genres = genres?.map { it.toDomain() }.orEmpty(),
    status = status.orEmpty(),
)

fun ReviewDto.toDomain() = Review(
    id = id.orEmpty(),
    author = author.orEmpty(),
    content = content.orEmpty(),
    createdAt = createdAt.orEmpty(),
    rating = authorDetails?.rating,
)

fun VideoDto.toDomain() = Video(
    id = id.orEmpty(),
    key = key.orEmpty(),
    name = name.orEmpty(),
    site = site.orEmpty(),
    type = type.orEmpty(),
)

fun CastDto.toDomain() = Cast(
    id = id ?: 0,
    name = name.orEmpty(),
    character = character.orEmpty(),
    profilePath = profilePath,
)

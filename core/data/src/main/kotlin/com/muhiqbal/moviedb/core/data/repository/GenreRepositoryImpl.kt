package com.muhiqbal.moviedb.core.data.repository

import com.muhiqbal.moviedb.core.common.result.toAppException
import com.muhiqbal.moviedb.core.data.mapper.toDomain
import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.repository.GenreRepository
import com.muhiqbal.moviedb.core.network.TmdbApiService
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
) : GenreRepository {

    override suspend fun getGenres(): Result<List<Genre>> =
        try {
            val response = apiService.getGenres()
            val genres = response.genres?.map { it.toDomain() }.orEmpty()
            Result.success(genres)
        } catch (e: Exception) {
            Result.failure(e.toAppException())
        }
}

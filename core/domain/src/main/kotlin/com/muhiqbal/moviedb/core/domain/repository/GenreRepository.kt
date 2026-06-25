package com.muhiqbal.moviedb.core.domain.repository

import com.muhiqbal.moviedb.core.domain.model.Genre

interface GenreRepository {
    suspend fun getGenres(): Result<List<Genre>>
}

package com.muhiqbal.moviedb.core.testing.fake

import com.muhiqbal.moviedb.core.domain.model.Genre
import com.muhiqbal.moviedb.core.domain.repository.GenreRepository

class FakeGenreRepository : GenreRepository {

    var genresResult: Result<List<Genre>> = Result.success(
        listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 18, name = "Drama"),
        )
    )
    var getGenresCallCount = 0

    override suspend fun getGenres(): Result<List<Genre>> {
        getGenresCallCount++
        return genresResult
    }
}

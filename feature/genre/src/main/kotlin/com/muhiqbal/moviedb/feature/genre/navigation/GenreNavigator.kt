package com.muhiqbal.moviedb.feature.genre.navigation

interface GenreNavigator {
    fun navigateToMovieList(genreId: Int, genreName: String)
    fun navigateToMovieDetail(movieId: Int)
}

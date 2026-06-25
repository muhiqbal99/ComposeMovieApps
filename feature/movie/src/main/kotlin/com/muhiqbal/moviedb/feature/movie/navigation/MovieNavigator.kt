package com.muhiqbal.moviedb.feature.movie.navigation

interface MovieNavigator {
    fun navigateToMovieDetail(movieId: Int)
    fun navigateBack()
}

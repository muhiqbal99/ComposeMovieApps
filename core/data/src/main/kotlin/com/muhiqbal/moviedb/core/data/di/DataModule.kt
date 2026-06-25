package com.muhiqbal.moviedb.core.data.di

import com.muhiqbal.moviedb.core.data.repository.GenreRepositoryImpl
import com.muhiqbal.moviedb.core.data.repository.MovieRepositoryImpl
import com.muhiqbal.moviedb.core.domain.repository.GenreRepository
import com.muhiqbal.moviedb.core.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindGenreRepository(impl: GenreRepositoryImpl): GenreRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}

package com.muhiqbal.moviedb.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muhiqbal.moviedb.core.common.result.toAppException
import com.muhiqbal.moviedb.core.data.mapper.toDomain
import com.muhiqbal.moviedb.core.domain.model.Movie
import com.muhiqbal.moviedb.core.network.TmdbApiService

class MoviePagingSource(
    private val apiService: TmdbApiService,
    private val genreId: Int,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE
        return try {
            val response = apiService.discoverMovies(genreId = genreId, page = page)
            val movies = response.results?.map { it.toDomain() }.orEmpty()
            val totalPages = response.totalPages ?: 1
            LoadResult.Page(
                data = movies,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (page >= totalPages) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e.toAppException())
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}

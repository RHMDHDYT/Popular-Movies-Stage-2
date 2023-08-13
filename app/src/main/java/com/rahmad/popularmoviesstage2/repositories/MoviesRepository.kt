package com.rahmad.popularmoviesstage2.repositories

import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import retrofit2.Response

class MoviesRepository(
    private val dataLocal: MoviesRepositoryContract,
    private val dataRemote: MoviesRepositoryContract
) : MoviesRepositoryContract {
    override suspend fun getMovies(
        sortBy: String
    ): Response<MovieResponse> {
        return dataRemote.getMovies(sortBy)
    }

    override fun getSortingState(): Int {
        return dataLocal.getSortingState()
    }

    override fun setSortingState(sortingState: Int) {
        dataLocal.setSortingState(sortingState)
    }

    override fun getFavoritedMovies(): List<MovieResultsItem> {
        return dataLocal.getFavoritedMovies()
    }
}
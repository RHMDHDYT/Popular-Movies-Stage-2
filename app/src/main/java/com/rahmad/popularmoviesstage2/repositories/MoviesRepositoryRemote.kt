package com.rahmad.popularmoviesstage2.repositories

import com.rahmad.popularmoviesstage2.services.MoviesService
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import com.rahmad.popularmoviesstage2.util.ApiInterface
import retrofit2.Response

class MoviesRepositoryRemote(private val apiService: MoviesService): MoviesRepositoryContract {
    override suspend fun getMovies(
        sortBy: String
    ): Response<MovieResponse> {
        return apiService.getMovies(sortBy)
    }

    override fun getSortingState(): Int {
        TODO("Not yet implemented")
    }

    override fun setSortingState(sortingState: Int) {
        TODO("Not yet implemented")
    }

    override fun getFavoritedMovies(): List<MovieResultsItem> {
        TODO("Not yet implemented")
    }
}
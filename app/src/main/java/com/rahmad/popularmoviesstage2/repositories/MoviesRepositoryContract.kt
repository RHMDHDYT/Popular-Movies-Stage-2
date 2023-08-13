package com.rahmad.popularmoviesstage2.repositories

import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import retrofit2.Response

interface MoviesRepositoryContract {
    suspend fun getMovies(
        sortBy: String
    ) : Response<MovieResponse>

    fun getSortingState(): Int
    fun setSortingState(sortingState: Int)
    fun getFavoritedMovies(): List<MovieResultsItem>
}
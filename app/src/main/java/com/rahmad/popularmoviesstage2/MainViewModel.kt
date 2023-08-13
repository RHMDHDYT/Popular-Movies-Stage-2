package com.rahmad.popularmoviesstage2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahmad.popularmoviesstage2.BuildConfig
import com.rahmad.popularmoviesstage2.repositories.MoviesRepositoryContract
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val moviesRepository: MoviesRepositoryContract) : ViewModel() {

    private val _moviesList = MutableLiveData<List<MovieResultsItem>>()
    val moviesList: LiveData<List<MovieResultsItem>> get() = _moviesList

    suspend fun getMoviesData(sortBy: String) {
        coroutineScope {
            try {
                _moviesList.value = moviesRepository.getMovies(sortBy).body()?.results ?: emptyList()
            } catch (e: Exception) {
                // Handle failure here
            }

        }
    }
}

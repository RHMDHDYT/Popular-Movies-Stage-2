package com.rahmad.popularmoviesstage2.services

import com.rahmad.popularmoviesstage2.BuildConfig
import com.rahmad.popularmoviesstage2.services.responses.moviedetail.MovieDetail
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.reviews.Reviews
import com.rahmad.popularmoviesstage2.services.responses.trailer.Trailer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/{sort}")
    suspend fun getMovies(
        @Path("sort") order: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetail>

    @GET("movie/{id}/reviews")
    suspend fun getReviews(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<Reviews>

    @GET("movie/{id}/videos")
    suspend fun getTrailer(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<Trailer>

}
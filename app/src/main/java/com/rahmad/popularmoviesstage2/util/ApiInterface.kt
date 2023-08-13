package com.rahmad.popularmoviesstage2.util

import com.rahmad.popularmoviesstage2.services.responses.moviedetail.MovieDetail
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.reviews.Reviews
import com.rahmad.popularmoviesstage2.services.responses.trailer.Trailer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
interface ApiInterface {
    @GET("movie/{sort}")
    fun getMovies(
        @Path("sort") order: String?,
        @Query("api_key") apiKey: String?
    ): Call<MovieResponse>?

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int, @Query("api_key") apiKey: String?): Call<MovieDetail?>?

    @GET("movie/{id}/reviews")
    fun getReviews(@Path("id") id: Int, @Query("api_key") apiKey: String?): Call<Reviews?>?

    @GET("movie/{id}/videos")
    fun getTrailer(@Path("id") id: Int, @Query("api_key") apiKey: String?): Call<Trailer?>?
}
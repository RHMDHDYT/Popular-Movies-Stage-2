package com.rahmad.popularmoviesstage1.util;

import com.rahmad.popularmoviesstage1.models.moviedetail.MovieDetail;
import com.rahmad.popularmoviesstage1.models.movielist.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

@SuppressWarnings("SameParameterValue") public interface ApiInterface {
  @GET("movie/top_rated") Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

  @GET("movie/popular") Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

  @GET("movie/{id}") Call<MovieDetail> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}

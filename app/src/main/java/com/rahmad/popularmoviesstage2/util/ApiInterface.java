package com.rahmad.popularmoviesstage2.util;

import com.rahmad.popularmoviesstage2.models.moviedetail.MovieDetail;
import com.rahmad.popularmoviesstage2.models.movielist.MovieResponse;
import com.rahmad.popularmoviesstage2.models.reviews.Reviews;
import com.rahmad.popularmoviesstage2.models.trailer.Trailer;
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
  @GET("movie/{sort}") Call<MovieResponse> getMovies(@Path("sort") String order, @Query("api_key") String apiKey);

  @GET("movie/{id}") Call<MovieDetail> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/reviews") Call<Reviews> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/videos") Call<Trailer> getTrailer(@Path("id") int id, @Query("api_key") String apiKey);
}

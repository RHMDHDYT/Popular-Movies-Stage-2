package com.rahmad.popularmoviesstage1.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

public class ApiClient {

  private static final String BASE_URL = "http://api.themoviedb.org/3/";
  private static Retrofit retrofit = null;

  public static Retrofit getClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    return retrofit;
  }
}

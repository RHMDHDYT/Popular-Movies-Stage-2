package com.rahmad.popularmoviesstage1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.rahmad.popularmoviesstage1.models.moviedetail.ModelMovieDetail;
import com.rahmad.popularmoviesstage1.models.moviedetail.MovieDetail;
import com.rahmad.popularmoviesstage1.util.ApiClient;
import com.rahmad.popularmoviesstage1.util.ApiInterface;
import com.rahmad.popularmoviesstage1.util.DateFormatter;
import com.rahmad.popularmoviesstage1.util.NetworkUtil;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rahmad on 7/7/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

public class DetailActivity extends AppCompatActivity {

  private Call<MovieDetail> callMovieDetail;
  private TextView textTitle;
  private TextView textSynopsisCaption;
  private TextView textSynopsisContent;
  private ImageView imageBackDrop;
  private ImageView imagePoster;
  private TextView textRatingContent;
  private TextView textDateRelease;
  private RatingBar ratingBar;
  private static final String KEY_SAVED_INSTANCE_STATE = "movie_detail_key";
  private ModelMovieDetail modelMovieDetail = new ModelMovieDetail();
  private ProgressDialog progressDialog;
  private AlertDialog.Builder builder;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    textTitle = (TextView) findViewById(R.id.textMovieTitle);
    textSynopsisCaption = (TextView) findViewById(R.id.textSynopsisCaption);
    textSynopsisContent = (TextView) findViewById(R.id.textSynopsisContent);
    imageBackDrop = (ImageView) findViewById(R.id.imageViewBackDrop);
    imagePoster = (ImageView) findViewById(R.id.imageViewPoster);
    textRatingContent = (TextView) findViewById(R.id.textRatingCaption);
    textDateRelease = (TextView) findViewById(R.id.textDateRelease);
    ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    progressDialog = new ProgressDialog(this);
    builder = new AlertDialog.Builder(this);
    builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    //set actionbar button
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    //set title
    setTitle(getString(R.string.detail_movie_caption));

    //set api caller
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    Intent intent = getIntent();

    if (intent.hasExtra(ConstantData.MOVIE_ID_KEY)) {
      Integer movieId = Integer.parseInt(intent.getStringExtra(ConstantData.MOVIE_ID_KEY));

      getDataDetail(apiService, movieId, savedInstanceState);
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(KEY_SAVED_INSTANCE_STATE, modelMovieDetail);

    super.onSaveInstanceState(outState);
  }

  private void getDataDetail(ApiInterface apiService, int movieId, Bundle savedInstanceState) {

    callMovieDetail = apiService.getMovieDetails(movieId, BuildConfig.API_KEY);
    if (NetworkUtil.isOnline(this)) {

      //null and check saved instance state
      if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_INSTANCE_STATE)) {
        showProgressBar();

        callMovieDetail.clone().enqueue(new Callback<MovieDetail>() {
          @Override public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
            hideProgressBar();

            MovieDetail model = response.body();

            if (model != null) {

              mappingNecessaryData(model);
              showData(modelMovieDetail);
            } else {
              showNoDataCaption();
            }
          }

          @Override public void onFailure(Call<MovieDetail> call, Throwable t) {
            hideProgressBar();
            showFailedCaption();
          }
        });
      } else {
        hideProgressBar();

        modelMovieDetail = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE_STATE);
        showData(modelMovieDetail);
      }
    } else {
      showNoConnectivityCaption();
    }
  }

  private void showData(ModelMovieDetail modelMovieDetail) {
    textTitle.setText(modelMovieDetail.getOriginalTitle());
    textDateRelease.setText(DateFormatter.getYear(modelMovieDetail.getReleaseDate()));
    textRatingContent.setText(getString(R.string.rating_placeholder_caption, modelMovieDetail.getVoteAverage()));
    textSynopsisCaption.setText(getString(R.string.synopsis_caption));
    textSynopsisContent.setText(modelMovieDetail.getOverview());
    Double ratingValue = modelMovieDetail.getVoteAverage() / 2;
    ratingBar.setRating(ratingValue.floatValue());

    String baseUrl = "http://image.tmdb.org/t/p/";
    String imageSize = "w185";
    String backdropPath = modelMovieDetail.getBackdropPath();
    String posterPath = modelMovieDetail.getPosterPath();

    Picasso.with(getApplicationContext()).load(baseUrl + imageSize + backdropPath).into(imageBackDrop);
    Picasso.with(getApplicationContext()).load(baseUrl + imageSize + posterPath).into(imagePoster);
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  private void showProgressBar() {
    progressDialog.setMessage(getString(R.string.loading_caption));
    progressDialog.show();
  }

  private void hideProgressBar() {
    if (progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  private void showNoConnectivityCaption() {
    builder.setMessage(getString(R.string.no_connectivity_caption));
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showNoDataCaption() {
    builder.setMessage(getString(R.string.no_data_caption));
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showFailedCaption() {
    builder.setMessage(getString(R.string.failed_getting_data_caption));
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void mappingNecessaryData(MovieDetail movieDetail) {

    modelMovieDetail.setOriginalTitle(movieDetail.getOriginalTitle());
    modelMovieDetail.setBackdropPath(movieDetail.getBackdropPath());
    modelMovieDetail.setPosterPath(movieDetail.getPosterPath());
    modelMovieDetail.setOverview(movieDetail.getOverview());
    modelMovieDetail.setVoteAverage(movieDetail.getVoteAverage());
    modelMovieDetail.setReleaseDate(movieDetail.getReleaseDate());
  }
}

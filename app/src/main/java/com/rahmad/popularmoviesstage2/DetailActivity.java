package com.rahmad.popularmoviesstage2;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry;
import com.rahmad.popularmoviesstage2.models.moviedetail.ModelMovieDetail;
import com.rahmad.popularmoviesstage2.models.moviedetail.MovieDetail;
import com.rahmad.popularmoviesstage2.models.reviews.ReviewResultsItem;
import com.rahmad.popularmoviesstage2.models.reviews.Reviews;
import com.rahmad.popularmoviesstage2.models.trailer.Trailer;
import com.rahmad.popularmoviesstage2.util.ApiClient;
import com.rahmad.popularmoviesstage2.util.ApiInterface;
import com.rahmad.popularmoviesstage2.util.DateFormatter;
import com.rahmad.popularmoviesstage2.util.NetworkUtil;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rahmad on 7/7/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

public class DetailActivity extends AppCompatActivity implements ReviewsAdapter.MoviesAdapterOnClickHandler {

  private TextView textTitle;
  private TextView textSynopsisCaption;
  private TextView textSynopsisContent;
  private ImageView imageBackDrop;
  private ImageView imagePoster;
  private TextView textRatingContent;
  private TextView textDateRelease;
  private RatingBar ratingBar;
  private RecyclerView recyclerViewReviews;
  private ReviewsAdapter reviewsAdapter;
  private List<ReviewResultsItem> reviewResultsItems = new ArrayList<>();
  private CollapsingToolbarLayout collapsingToolbarLayout;
  private static final String KEY_SAVED_INSTANCE_STATE = "movie_detail_key";
  private static final String KEY_SAVED_REVIEW_INSTANCE_STATE = "movie_reviews_key";
  private static final String KEY_SAVED_TRAILER_INSTANCE_STATE = "movie_trailer_key";
  private ModelMovieDetail modelMovieDetail = new ModelMovieDetail();
  private ProgressDialog progressDialog;
  private AlertDialog.Builder builder;
  private TextView textInfoCaption;
  private ProgressBar progressBar;
  private ImageView imagePlayIcon;
  private String idTrailer;
  private LikeButton buttonFavorite;
  private static final String YOUTUBE_ENDPOINT = "https://www.youtube.com/watch?v=";
  private Integer movieId;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
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
    collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    recyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerViewReviews);
    textInfoCaption = (TextView) findViewById(R.id.text_caption);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    imagePlayIcon = (ImageView) findViewById(R.id.imageViewPlayIcon);
    buttonFavorite = (LikeButton) findViewById(R.id.buttonFavorite);

    SnapHelper snapHelper = new LinearSnapHelper();
    snapHelper.attachToRecyclerView(recyclerViewReviews);
    reviewsAdapter = new ReviewsAdapter(reviewResultsItems, this, this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    linearLayoutManager.setAutoMeasureEnabled(true);
    recyclerViewReviews.setNestedScrollingEnabled(false);
    recyclerViewReviews.setHasFixedSize(false);
    recyclerViewReviews.setLayoutManager(linearLayoutManager);
    recyclerViewReviews.setAdapter(reviewsAdapter);
    progressDialog = new ProgressDialog(this);
    builder = new AlertDialog.Builder(this);
    builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    //set actionbar
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(null);
    //clear content
    clearContent();

    //set api caller
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    Intent intent = getIntent();

    if (intent.hasExtra(ConstantData.MOVIE_ID_KEY)) {
      movieId = Integer.parseInt(intent.getStringExtra(ConstantData.MOVIE_ID_KEY));
      //check favorite flag
      checkFavFlag();

      getDataDetail(apiService, movieId, savedInstanceState);
      getReviews(apiService, movieId, savedInstanceState);
      getTrailer(apiService, movieId, savedInstanceState);
    }

    imagePlayIcon.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
            Uri.parse(YOUTUBE_ENDPOINT + idTrailer));

        startActivity(browserIntent);
      }
    });

    buttonFavorite.setOnLikeListener(new OnLikeListener() {
      @Override
      public void liked(LikeButton likeButton) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(FavoriteEntry.COLUMN_TITLE, modelMovieDetail.getOriginalTitle());
        contentValues.put(FavoriteEntry.COLUMN_THUMBNAIL, modelMovieDetail.getPosterPath());

//        getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);

        Cursor cursor = getContentResolver()
            .query(FavoriteEntry.CONTENT_URI, null, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                movieId.toString(), null, null);

        if (cursor != null) {
          if (cursor.getCount() == 0) {
            getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);
          } else {
            getContentResolver().update(FavoriteEntry.CONTENT_URI, contentValues, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                movieId.toString(), null);
          }
        }


      }

      @Override
      public void unLiked(LikeButton likeButton) {
        getContentResolver().delete(FavoriteEntry.CONTENT_URI, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
            movieId.toString(), null);

      }
    });
  }

  private void checkFavFlag() {
    Cursor cursor = getContentResolver()
        .query(FavoriteEntry.CONTENT_URI, null, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
            movieId.toString(), null, null);

    if (cursor != null) {
      if (cursor.getCount() > 0) {
        buttonFavorite.setLiked(true);
      } else {
        buttonFavorite.setLiked(false);
      }
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(KEY_SAVED_INSTANCE_STATE, modelMovieDetail);
    outState.putParcelableArrayList(KEY_SAVED_REVIEW_INSTANCE_STATE,
        (ArrayList<? extends Parcelable>) reviewResultsItems);
    outState.putString(KEY_SAVED_TRAILER_INSTANCE_STATE, idTrailer);

    super.onSaveInstanceState(outState);
  }

  private void clearContent() {
    textTitle.setText(null);
    textDateRelease.setText(null);
    textRatingContent.setText(null);
    textSynopsisCaption.setText(null);
    textSynopsisContent.setText(null);
    ratingBar.setRating(0);
    imageBackDrop.setImageResource(0);
    imagePoster.setImageResource(0);
  }

  private void getDataDetail(ApiInterface apiService, int movieId, Bundle savedInstanceState) {

    Log.d("Movie Id", String.valueOf(movieId));
    Call<MovieDetail> callMovieDetail = apiService.getMovieDetails(movieId, BuildConfig.API_KEY);

    if (NetworkUtil.isOnline(this)) {

      //null and check saved instance state
      if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_INSTANCE_STATE)) {
        showProgressBar();

        callMovieDetail.clone().enqueue(new Callback<MovieDetail>() {
          @Override
          public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
            hideProgressBar();
            Log.d("Response Detail", response.body().toString());
            MovieDetail model = response.body();

            if (model != null) {

              mappingNecessaryData(model);
              showData(modelMovieDetail);
            } else {
              showNoDataCaption();
            }
          }

          @Override
          public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
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

  private void getReviews(ApiInterface apiService, final int movieId, Bundle savedInstanceState) {
    Call<Reviews> callReviews = apiService.getReviews(movieId, BuildConfig.API_KEY);

    if (NetworkUtil.isOnline(this)) {

      //null and check saved instance state
      if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_REVIEW_INSTANCE_STATE)) {
        showProgressReviewBar();

        callReviews.clone().enqueue(new Callback<Reviews>() {
          @Override
          public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {
            hideProgressReviewBar();
            Log.d("Response Detail", response.body().toString());
            Reviews model = response.body();

            if (model != null) {
              hideNoReviewsCaption();
              showReviewsData(model);
            } else {
              showNoReviewsCaption();
            }
          }

          @Override
          public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
            hideProgressReviewBar();
            showFailedGettingReviewsCaption();

          }
        });
      } else {
        hideProgressReviewBar();

        List<ReviewResultsItem> parcelableData = savedInstanceState
            .getParcelableArrayList(KEY_SAVED_REVIEW_INSTANCE_STATE);

        assert parcelableData != null;
        reviewResultsItems.addAll(parcelableData);

        if (reviewResultsItems.size() == 0) {
          showNoReviewsCaption();
          clearReviewListData();
        } else {
          hideNoReviewsCaption();
          reviewsAdapter.notifyDataSetChanged();
        }

      }
    } else {
      showFailedGettingReviewsCaption();
    }
  }

  private void getTrailer(ApiInterface apiService, int movieId, Bundle savedInstanceState) {

    Log.d("Movie Id", String.valueOf(movieId));
    Call<Trailer> callTrailer = apiService.getTrailer(movieId, BuildConfig.API_KEY);

    if (NetworkUtil.isOnline(this)) {
      if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_TRAILER_INSTANCE_STATE)) {

        callTrailer.clone().enqueue(new Callback<Trailer>() {
          @Override
          public void onResponse(@NonNull Call<Trailer> call, @NonNull Response<Trailer> response) {
            Log.d("Response Detail", response.body().toString());
            Trailer model = response.body();

            if (model != null) {
              mapTrailerData(model);
            }
          }

          @Override
          public void onFailure(@NonNull Call<Trailer> call, @NonNull Throwable t) {

          }
        });
      } else {
        idTrailer = savedInstanceState.getString(KEY_SAVED_TRAILER_INSTANCE_STATE);
      }
    }
  }

  private void mapTrailerData(Trailer model) {
    if (model.getResults().size() == 0) {
      hidePlayIcon();
    } else {
      showPlayIcon();
      idTrailer = model.getResults().get(0).getKey();
    }
  }

  private void showReviewsData(Reviews model) {
    if (model.getTotalResults() == 0) {
      showNoReviewsCaption();
    } else {
      reviewResultsItems.addAll(model.getResults());
      reviewsAdapter.notifyDataSetChanged();
    }
  }

  private void clearReviewListData() {
    reviewResultsItems.clear();
    reviewsAdapter.notifyDataSetChanged();
  }

  private void showPlayIcon() {
    imagePlayIcon.setVisibility(View.VISIBLE);
  }

  private void hidePlayIcon() {
    imagePlayIcon.setVisibility(View.GONE);
  }

  private void showData(ModelMovieDetail modelMovieDetail) {
    collapsingToolbarLayout.setTitle(modelMovieDetail.getOriginalTitle());
    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
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
    String backdropUrl = baseUrl + imageSize + backdropPath;
    String posterUrl = baseUrl + imageSize + posterPath;

    Picasso.with(getApplicationContext()).load(backdropUrl).into(imageBackDrop);
    Log.d("Backdrop Url", backdropUrl);
    Picasso.with(getApplicationContext()).load(posterUrl).into(imagePoster);
    Log.d("Poster Url", posterUrl);
  }

  @Override
  public boolean onSupportNavigateUp() {
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

  private void showNoReviewsCaption() {
    textInfoCaption.setText(getString(R.string.no_data_reviews_caption));
    textInfoCaption.setVisibility(View.VISIBLE);
  }

  private void showFailedGettingReviewsCaption() {
    textInfoCaption.setText(getString(R.string.failed_getting_data_caption));
    textInfoCaption.setVisibility(View.VISIBLE);
  }

  private void hideNoReviewsCaption() {
    textInfoCaption.setVisibility(View.GONE);
  }

  private void showProgressReviewBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideProgressReviewBar() {
    progressBar.setVisibility(View.GONE);
  }

  private void mappingNecessaryData(MovieDetail movieDetail) {

    modelMovieDetail.setOriginalTitle(movieDetail.getOriginalTitle());
    modelMovieDetail.setBackdropPath(movieDetail.getBackdropPath());
    modelMovieDetail.setPosterPath(movieDetail.getPosterPath());
    modelMovieDetail.setOverview(movieDetail.getOverview());
    modelMovieDetail.setVoteAverage(movieDetail.getVoteAverage());
    modelMovieDetail.setReleaseDate(movieDetail.getReleaseDate());
  }

  @Override
  public void onClick(String data) {
    //click action for recyclerview reviews

//    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
//    startActivity(browserIntent);
  }
}

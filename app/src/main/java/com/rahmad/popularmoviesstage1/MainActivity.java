package com.rahmad.popularmoviesstage1;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.rahmad.popularmoviesstage1.models.movielist.MovieResponse;
import com.rahmad.popularmoviesstage1.models.movielist.MovieResultsItem;
import com.rahmad.popularmoviesstage1.util.ApiClient;
import com.rahmad.popularmoviesstage1.util.ApiInterface;
import com.rahmad.popularmoviesstage1.util.AppSharedPref;
import com.rahmad.popularmoviesstage1.util.NetworkUtil;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private TextView textInfoCaption;
  private RecyclerView listMovies;
  private ProgressBar progressBar;
  private static final int COLUMN_SIZE = 2;
  private Call<MovieResponse> callTopRatedMovies;
  private Call<MovieResponse> callPopularMovies;
  private static final String KEY_SAVED_INSTANCE_STATE = "movie_poster_key";
  private List<MovieResultsItem> moviesList = new ArrayList<>();
  private MoviesAdapter moviesAdapter;
  private SwipeRefreshLayout swipeContainer;
  private Boolean isCurrentPopular;
  private AppSharedPref appSharedPref;

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textInfoCaption = (TextView) findViewById(R.id.text_caption);
    listMovies = (RecyclerView) findViewById(R.id.grid_poster_movies);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

    appSharedPref = new AppSharedPref(this);
    isCurrentPopular = appSharedPref.getIsPopularState();
    setToolbarTitle();

    moviesAdapter = new MoviesAdapter(moviesList, getApplicationContext());
    listMovies.setHasFixedSize(true);
    listMovies.setLayoutManager(new GridLayoutManager(this, COLUMN_SIZE));
    listMovies.setAdapter(moviesAdapter);

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    callTopRatedMovies = apiService.getTopRatedMovies(BuildConfig.API_KEY);
    callPopularMovies = apiService.getPopularMovies(BuildConfig.API_KEY);

    getMoviesData(savedInstanceState);

    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        getMoviesData(null, false);
      }
    });
  }

  private void setToolbarTitle() {
    if (isCurrentPopular) {
      setTitle(getString(R.string.most_popular_movies_caption));
    } else {
      setTitle(getString(R.string.highest_rated_movies_caption));
    }
  }

  private void getMoviesData(Bundle savedInstanceState) {
    getMoviesData(savedInstanceState, true);
  }

  private void getMoviesData(Bundle savedInstanceState, Boolean enableLoadingIndicatorCenter) {
    if (NetworkUtil.isOnline(this)) {
      hideTextCaption();

      //null and check saved instance state
      if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_INSTANCE_STATE)) {
        if (isCurrentPopular) {
          getPopularMovies(enableLoadingIndicatorCenter);
        } else {
          getTopRatedMovies(enableLoadingIndicatorCenter);
        }
      } else {
        List<MovieResultsItem> parcelableData = savedInstanceState.getParcelableArrayList(KEY_SAVED_INSTANCE_STATE);
        moviesList.addAll(parcelableData);
        if (moviesList == null || moviesList.size() == 0) {
          showNoDataCaption();
          clearListData();
        } else {
          hideTextCaption();
          moviesAdapter.notifyDataSetChanged();
        }
      }
    } else {
      showNoConnectivityCaption();
      clearListData();
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList(KEY_SAVED_INSTANCE_STATE, (ArrayList<? extends Parcelable>) moviesList);
    appSharedPref.setIsPopularState(isCurrentPopular);
    appSharedPref.commit();
    super.onSaveInstanceState(outState);
  }

  private void getTopRatedMovies(Boolean withLoading) {
    hideTextCaption();

    if (withLoading) {
      showProgressBar();
    }

    callTopRatedMovies.clone().enqueue(new Callback<MovieResponse>() {
      @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        processSuccessData(response);
      }

      @Override public void onFailure(Call<MovieResponse> call, Throwable t) {
        processFailedData(t);
      }
    });
  }

  private void getPopularMovies(Boolean withLoading) {
    hideTextCaption();

    if (withLoading) {
      showProgressBar();
    }

    callPopularMovies.clone().enqueue(new Callback<MovieResponse>() {
      @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        processSuccessData(response);
      }

      @Override public void onFailure(Call<MovieResponse> call, Throwable t) {
        processFailedData(t);
      }
    });
  }

  private void processSuccessData(Response<MovieResponse> response) {
    swipeContainer.setRefreshing(false);
    hideProgressBar();

    if (response.body() == null || response.body().getResults().size() == 0) {
      showNoDataCaption();
    } else {
      hideTextCaption();
      moviesList.clear();
      moviesList.addAll(response.body().getResults());
    }

    moviesAdapter.notifyDataSetChanged();
  }

  private void processFailedData(Throwable t) {
    swipeContainer.setRefreshing(false);
    hideProgressBar();
    showFailedCaption();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.option_menu, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_highest_rated) {
      setTitle(getString(R.string.highest_rated_movies_caption));
      isCurrentPopular = false;
      clearListData();
      getMoviesData(null);
      return true;
    } else if (id == R.id.action_most_popular) {
      isCurrentPopular = true;
      setTitle(getString(R.string.most_popular_movies_caption));
      clearListData();
      getMoviesData(null);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void clearListData() {
    moviesList.clear();
    moviesAdapter.notifyDataSetChanged();
  }

  private void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  private void showNoConnectivityCaption() {
    textInfoCaption.setText(getString(R.string.no_connectivity_caption));
    textInfoCaption.setVisibility(View.VISIBLE);
  }

  private void showNoDataCaption() {
    textInfoCaption.setText(getString(R.string.no_data_caption));
    textInfoCaption.setVisibility(View.VISIBLE);
  }

  private void showFailedCaption() {
    textInfoCaption.setText(getString(R.string.failed_getting_data_caption));
    textInfoCaption.setVisibility(View.VISIBLE);
  }

  private void hideTextCaption() {
    textInfoCaption.setVisibility(View.GONE);
  }
}

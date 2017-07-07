package com.rahmad.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rahmad.popularmoviesstage1.models.movielist.MovieResultsItem;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

  private List<MovieResultsItem> moviesList;
  private Context context;

  public static class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView moviePoster;

    public MovieViewHolder(View v) {
      super(v);
      moviePoster = (ImageView) v.findViewById(R.id.poster_image);
    }
  }

  public MoviesAdapter(List<MovieResultsItem> moviesList, Context context) {
    this.moviesList = moviesList;
    this.context = context;
  }

  @Override public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    int layoutListItem = R.layout.recyclerview_poster_item;
    View view = LayoutInflater.from(parent.getContext()).inflate(layoutListItem, parent, false);
    return new MovieViewHolder(view);
  }

  @Override public void onBindViewHolder(MovieViewHolder holder, final int position) {
    String baseUrl = "http://image.tmdb.org/t/p/";
    String imageSize = "w185";
    String imagePath = moviesList.get(position).getPosterPath();

    Picasso.with(context).load(baseUrl + imageSize + imagePath).into(holder.moviePoster);
  }

  @Override public int getItemCount() {
    return moviesList.size();
  }
}

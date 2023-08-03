package com.rahmad.popularmoviesstage2;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rahmad.popularmoviesstage2.models.movielist.MovieResultsItem;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

  private final List<MovieResultsItem> moviesList;
  private final Context context;
  private final MoviesAdapterOnClickHandler mClickHandler;

  /**
   * The interface that receives onClick messages.
   */
  public interface MoviesAdapterOnClickHandler {
    /**
     * On click.
     *
     * @param data the data
     */
    void onClick(String data);
  }

  /**
   * The type Movie view holder.
   */
  public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /**
     * The Movie poster.
     */
    final ImageView moviePoster;

    /**
     * Instantiates a new Movie view holder.
     *
     * @param v the v
     */
    public MovieViewHolder(View v) {
      super(v);
      moviePoster = (ImageView) v.findViewById(R.id.poster_image);

      v.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      String weatherForDay = String.valueOf(moviesList.get(adapterPosition).getId());
      mClickHandler.onClick(weatherForDay);
    }
  }

  /**
   * Instantiates a new Movies adapter.
   *
   * @param moviesList the movies list
   * @param context the context
   * @param clickHandler the click handler
   */
  public MoviesAdapter(List<MovieResultsItem> moviesList, Context context,MoviesAdapterOnClickHandler clickHandler ) {
    this.moviesList = moviesList;
    this.context = context;
    mClickHandler = clickHandler;
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

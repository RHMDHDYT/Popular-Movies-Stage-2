package com.rahmad.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rahmad.popularmoviesstage2.models.reviews.ReviewResultsItem;
import java.util.List;

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MovieViewHolder> {

  private final List<ReviewResultsItem> reviewList;
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
    final LinearLayout layoutReviewItem;
    final TextView textUserName;
    final TextView textReviewContent;

    /**
     * Instantiates a new Movie view holder.
     *
     * @param v the v
     */
    public MovieViewHolder(View v) {
      super(v);
      layoutReviewItem = (LinearLayout) v.findViewById(R.id.layoutReviewItem);
      textUserName = (TextView) v.findViewById(R.id.textUserName);
      textReviewContent = (TextView) v.findViewById(R.id.textReviewContent);

      v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      String url = String.valueOf(reviewList.get(adapterPosition).getUrl());
      mClickHandler.onClick(url);
    }
  }

  /**
   * Instantiates a new Movies adapter.
   *
   * @param reviewList the movies list
   * @param context the context
   * @param clickHandler the click handler
   */
  public ReviewsAdapter(List<ReviewResultsItem> reviewList, Context context, MoviesAdapterOnClickHandler clickHandler) {
    this.reviewList = reviewList;
    this.context = context;
    mClickHandler = clickHandler;
  }

  @Override
  public ReviewsAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    int layoutListItem = R.layout.recyclerview_review_item;
    View view = LayoutInflater.from(parent.getContext()).inflate(layoutListItem, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, final int position) {

    holder.textUserName.setText(reviewList.get(position).getAuthor());
    holder.textReviewContent.setText(reviewList.get(position).getContent());

  }

  @Override
  public int getItemCount() {
    return reviewList.size();
  }
}

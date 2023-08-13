package com.rahmad.popularmoviesstage2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahmad.popularmoviesstage2.services.responses.reviews.ReviewResultsItem

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
class ReviewsAdapter(
    private val reviewList: List<ReviewResultsItem>,
    private val mClickHandler: MoviesAdapterOnClickHandler
) : RecyclerView.Adapter<ReviewsAdapter.MovieViewHolder>() {

    interface MoviesAdapterOnClickHandler {
        fun onClick(data: String?)
    }

    inner class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val layoutReviewItem: LinearLayout
        val textUserName: TextView
        val textReviewContent: TextView

        init {
            layoutReviewItem = v.findViewById<View>(R.id.layoutReviewItem) as LinearLayout
            textUserName = v.findViewById<View>(R.id.textUserName) as TextView
            textReviewContent = v.findViewById<View>(R.id.textReviewContent) as TextView
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val url = reviewList[adapterPosition].url.toString()
            mClickHandler.onClick(url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutListItem = R.layout.recyclerview_review_item
        val view = LayoutInflater.from(parent.context).inflate(layoutListItem, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.textUserName.text = reviewList[position].author
        holder.textReviewContent.text = reviewList[position].content
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}
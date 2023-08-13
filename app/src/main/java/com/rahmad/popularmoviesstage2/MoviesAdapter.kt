package com.rahmad.popularmoviesstage2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import com.squareup.picasso.Picasso

/**
 * Created by rahmad on 7/2/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
class MoviesAdapter(
    private val moviesList: MutableList<MovieResultsItem>,
    private val context: Context,
    private val mClickHandler: MoviesAdapterOnClickHandler
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    interface MoviesAdapterOnClickHandler {
        fun onClick(data: String?)
    }

    inner class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val moviePoster: ImageView

        init {
            moviePoster = v.findViewById<View>(R.id.poster_image) as ImageView
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val weatherForDay = moviesList[adapterPosition].id.toString()
            mClickHandler.onClick(weatherForDay)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutListItem = R.layout.recyclerview_poster_item
        val view = LayoutInflater.from(parent.context).inflate(layoutListItem, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val baseUrl = "http://image.tmdb.org/t/p/"
        val imageSize = "w185"
        val imagePath = moviesList[position].posterPath
        Picasso.with(context).load(baseUrl + imageSize + imagePath).into(holder.moviePoster)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}
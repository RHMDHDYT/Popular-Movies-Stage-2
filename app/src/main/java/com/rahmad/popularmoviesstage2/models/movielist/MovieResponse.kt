package com.rahmad.popularmoviesstage2.models.movielist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("results")
    val results: List<MovieResultsItem> = mutableListOf(),
    @SerializedName("total_results")
    val totalResults: Int = 0,
) : Parcelable
package com.rahmad.popularmoviesstage2.models.reviews

import com.google.gson.annotations.SerializedName

data class Reviews(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("results")
    val results: List<ReviewResultsItem> = listOf(),
    @SerializedName("total_results")
    val totalResults: Int = 0,
)
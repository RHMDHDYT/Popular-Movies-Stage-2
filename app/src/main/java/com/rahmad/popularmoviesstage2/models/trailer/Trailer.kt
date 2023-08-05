package com.rahmad.popularmoviesstage2.models.trailer

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("results")
    val results: List<TrailerResultsItem> = listOf()
)
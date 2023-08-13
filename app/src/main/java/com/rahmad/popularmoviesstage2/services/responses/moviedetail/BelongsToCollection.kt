package com.rahmad.popularmoviesstage2.services.responses.moviedetail

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class BelongsToCollection(
    @SerializedName("backdrop_path")
    private val backdropPath: String? = null,
    @SerializedName("name")
    private val name: String? = null,
    @SerializedName("id")
    private val id: Int = 0,
    @SerializedName("poster_path")
    private val posterPath: String? = null,
)
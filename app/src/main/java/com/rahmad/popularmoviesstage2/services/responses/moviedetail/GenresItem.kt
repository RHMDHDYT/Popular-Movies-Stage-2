package com.rahmad.popularmoviesstage2.services.responses.moviedetail

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class GenresItem(
    @SerializedName("name")
    private val name: String? = null,
    @SerializedName("id")
    private val id: Int = 0,
)
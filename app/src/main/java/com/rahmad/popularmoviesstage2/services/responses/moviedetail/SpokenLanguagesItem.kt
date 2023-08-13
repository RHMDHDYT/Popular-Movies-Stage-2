package com.rahmad.popularmoviesstage2.services.responses.moviedetail

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class SpokenLanguagesItem(
    @SerializedName("name")
    private val name: String? = null,
    @SerializedName("iso_639_1")
    private val iso6391: String? = null
)
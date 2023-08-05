package com.rahmad.popularmoviesstage2.models.moviedetail

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class ProductionCountriesItem(
    @SerializedName("iso_3166_1")
    private val iso31661: String? = null,
    @SerializedName("name")
    private val name: String? = null
)
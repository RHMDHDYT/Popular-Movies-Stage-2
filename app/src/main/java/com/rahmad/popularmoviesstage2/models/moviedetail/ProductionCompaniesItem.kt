package com.rahmad.popularmoviesstage2.models.moviedetail

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class ProductionCompaniesItem(
    @SerializedName("name")
    private val name: String? = null,
    @SerializedName("id")
    private val id: Int = 0,
)
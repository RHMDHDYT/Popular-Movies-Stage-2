package com.rahmad.popularmoviesstage2.services.responses.trailer

import com.google.gson.annotations.SerializedName

data class TrailerResultsItem(
    @SerializedName("site")
    val site: String = "",
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("iso_3166_1")
    val iso31661: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("iso_639_1")
    val iso6391: String = "",
    @SerializedName("key")
    val key: String = "",
)
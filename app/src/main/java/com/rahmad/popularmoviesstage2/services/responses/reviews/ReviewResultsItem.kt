package com.rahmad.popularmoviesstage2.services.responses.reviews

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResultsItem(
    @SerializedName("author")
    val author: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("url")
    val url: String = "",
) : Parcelable
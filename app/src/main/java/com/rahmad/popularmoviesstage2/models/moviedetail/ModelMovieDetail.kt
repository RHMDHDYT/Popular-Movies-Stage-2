package com.rahmad.popularmoviesstage2.models.moviedetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelMovieDetail(
    var backdropPath: String = "",
    var overview: String = "",
    var originalTitle: String = "",
    var releaseDate: String = "",
    var voteAverage: Double = 0.0,
    var posterPath: String = "",
) : Parcelable
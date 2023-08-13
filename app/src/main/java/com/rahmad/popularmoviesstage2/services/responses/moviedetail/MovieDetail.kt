package com.rahmad.popularmoviesstage2.services.responses.moviedetail

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("imdb_id")
    val imdbId: String = "",
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    @SerializedName("revenue")
    val revenue: Int = 0,
    @SerializedName("genres")
    val genres: List<GenresItem> = listOf(),
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountriesItem> = listOf(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("vote_count")
    val voteCount: Int = 0,
    @SerializedName("budget")
    val budget: Int = 0,
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    @SerializedName("runtime")
    val runtime: Int = 0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItem> = listOf(),
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem> = listOf(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection = BelongsToCollection(),
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("homepage")
    val homepage: String = "",
    @SerializedName("status")
    val status: String = ""
)
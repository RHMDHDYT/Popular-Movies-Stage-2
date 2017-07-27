package com.rahmad.popularmoviesstage2.models.moviedetail;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieDetail {

  @SerializedName("original_language") private String originalLanguage;

  @SerializedName("imdb_id") private String imdbId;

  @SerializedName("video") private boolean video;

  @SerializedName("title") private String title;

  @SerializedName("backdrop_path") private String backdropPath;

  @SerializedName("revenue") private int revenue;

  @SerializedName("genres") private List<GenresItem> genres;

  @SerializedName("popularity") private double popularity;

  @SerializedName("production_countries") private List<ProductionCountriesItem> productionCountries;

  @SerializedName("id") private int id;

  @SerializedName("vote_count") private int voteCount;

  @SerializedName("budget") private int budget;

  @SerializedName("overview") private String overview;

  @SerializedName("original_title") private String originalTitle;

  @SerializedName("runtime") private int runtime;

  @SerializedName("poster_path") private String posterPath;

  @SerializedName("spoken_languages") private List<SpokenLanguagesItem> spokenLanguages;

  @SerializedName("production_companies") private List<ProductionCompaniesItem> productionCompanies;

  @SerializedName("release_date") private String releaseDate;

  @SerializedName("vote_average") private double voteAverage;

  @SerializedName("belongs_to_collection") private BelongsToCollection belongsToCollection;

  @SerializedName("tagline") private String tagline;

  @SerializedName("adult") private boolean adult;

  @SerializedName("homepage") private String homepage;

  @SerializedName("status") private String status;

  @SuppressWarnings("unused") public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  @SuppressWarnings("unused") public String getOriginalLanguage() {
    return originalLanguage;
  }

  @SuppressWarnings("unused") public void setImdbId(String imdbId) {
    this.imdbId = imdbId;
  }

  @SuppressWarnings("unused") public String getImdbId() {
    return imdbId;
  }

  @SuppressWarnings("unused") public void setVideo(boolean video) {
    this.video = video;
  }

  @SuppressWarnings("unused") public boolean isVideo() {
    return video;
  }

  @SuppressWarnings("unused") public void setTitle(String title) {
    this.title = title;
  }

  @SuppressWarnings("unused") public String getTitle() {
    return title;
  }

  @SuppressWarnings("unused") public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  @SuppressWarnings("unused") public void setRevenue(int revenue) {
    this.revenue = revenue;
  }

  @SuppressWarnings("unused") public int getRevenue() {
    return revenue;
  }

  @SuppressWarnings("unused") public void setGenres(List<GenresItem> genres) {
    this.genres = genres;
  }

  @SuppressWarnings("unused") public List<GenresItem> getGenres() {
    return genres;
  }

  @SuppressWarnings("unused") public void setPopularity(double popularity) {
    this.popularity = popularity;
  }

  @SuppressWarnings("unused") public double getPopularity() {
    return popularity;
  }

  @SuppressWarnings("unused") public void setProductionCountries(List<ProductionCountriesItem> productionCountries) {
    this.productionCountries = productionCountries;
  }

  @SuppressWarnings("unused") public List<ProductionCountriesItem> getProductionCountries() {
    return productionCountries;
  }

  @SuppressWarnings("unused") public void setId(int id) {
    this.id = id;
  }

  @SuppressWarnings("unused") public int getId() {
    return id;
  }

  @SuppressWarnings("unused") public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  @SuppressWarnings("unused") public int getVoteCount() {
    return voteCount;
  }

  @SuppressWarnings("unused") public void setBudget(int budget) {
    this.budget = budget;
  }

  @SuppressWarnings("unused") public int getBudget() {
    return budget;
  }

  @SuppressWarnings("unused") public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getOverview() {
    return overview;
  }

  @SuppressWarnings("unused") public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  @SuppressWarnings("unused") public void setRuntime(int runtime) {
    this.runtime = runtime;
  }

  @SuppressWarnings("unused") public int getRuntime() {
    return runtime;
  }

  @SuppressWarnings("unused") public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getPosterPath() {
    return posterPath;
  }

  @SuppressWarnings("unused") public void setSpokenLanguages(List<SpokenLanguagesItem> spokenLanguages) {
    this.spokenLanguages = spokenLanguages;
  }

  @SuppressWarnings("unused") public List<SpokenLanguagesItem> getSpokenLanguages() {
    return spokenLanguages;
  }

  @SuppressWarnings("unused") public void setProductionCompanies(List<ProductionCompaniesItem> productionCompanies) {
    this.productionCompanies = productionCompanies;
  }

  @SuppressWarnings("unused") public List<ProductionCompaniesItem> getProductionCompanies() {
    return productionCompanies;
  }

  @SuppressWarnings("unused") public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  @SuppressWarnings("unused") public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  @SuppressWarnings("unused") public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
    this.belongsToCollection = belongsToCollection;
  }

  @SuppressWarnings("unused") public BelongsToCollection getBelongsToCollection() {
    return belongsToCollection;
  }

  @SuppressWarnings("unused") public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  @SuppressWarnings("unused") public String getTagline() {
    return tagline;
  }

  @SuppressWarnings("unused") public void setAdult(boolean adult) {
    this.adult = adult;
  }

  @SuppressWarnings("unused") public boolean isAdult() {
    return adult;
  }

  @SuppressWarnings("unused") public void setHomepage(String homepage) {
    this.homepage = homepage;
  }

  @SuppressWarnings("unused") public String getHomepage() {
    return homepage;
  }

  @SuppressWarnings("unused") public void setStatus(String status) {
    this.status = status;
  }

  @SuppressWarnings("unused") public String getStatus() {
    return status;
  }

  @Override public String toString() {
    return "MovieDetail{"
        + "original_language = '"
        + originalLanguage
        + '\''
        + ",imdb_id = '"
        + imdbId
        + '\''
        + ",video = '"
        + video
        + '\''
        + ",title = '"
        + title
        + '\''
        + ",backdrop_path = '"
        + backdropPath
        + '\''
        + ",revenue = '"
        + revenue
        + '\''
        + ",genres = '"
        + genres
        + '\''
        + ",popularity = '"
        + popularity
        + '\''
        + ",production_countries = '"
        + productionCountries
        + '\''
        + ",id = '"
        + id
        + '\''
        + ",vote_count = '"
        + voteCount
        + '\''
        + ",budget = '"
        + budget
        + '\''
        + ",overview = '"
        + overview
        + '\''
        + ",original_title = '"
        + originalTitle
        + '\''
        + ",runtime = '"
        + runtime
        + '\''
        + ",poster_path = '"
        + posterPath
        + '\''
        + ",spoken_languages = '"
        + spokenLanguages
        + '\''
        + ",production_companies = '"
        + productionCompanies
        + '\''
        + ",release_date = '"
        + releaseDate
        + '\''
        + ",vote_average = '"
        + voteAverage
        + '\''
        + ",belongs_to_collection = '"
        + belongsToCollection
        + '\''
        + ",tagline = '"
        + tagline
        + '\''
        + ",adult = '"
        + adult
        + '\''
        + ",homepage = '"
        + homepage
        + '\''
        + ",status = '"
        + status
        + '\''
        + "}";
  }
}
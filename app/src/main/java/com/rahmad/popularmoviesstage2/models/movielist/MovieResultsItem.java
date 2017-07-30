package com.rahmad.popularmoviesstage2.models.movielist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MovieResultsItem implements Parcelable {

  @SerializedName("overview") private String overview;

  @SerializedName("original_language") private String originalLanguage;

  @SerializedName("original_title") private String originalTitle;

  @SerializedName("video") private boolean video;

  @SerializedName("title") private String title;

  @SerializedName("genre_ids") private List<Integer> genreIds;

  @SerializedName("poster_path") private String posterPath;

  @SerializedName("backdrop_path") private String backdropPath;

  @SerializedName("release_date") private String releaseDate;

  @SerializedName("vote_average") private double voteAverage;

  @SerializedName("popularity") private double popularity;

  @SerializedName("id") private int id;

  @SerializedName("adult") private boolean adult;

  @SerializedName("vote_count") private int voteCount;

  @SuppressWarnings("unused") public void setOverview(String overview) {
    this.overview = overview;
  }

  @SuppressWarnings("unused") public String getOverview() {
    return overview;
  }

  @SuppressWarnings("unused") public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  @SuppressWarnings("unused") public String getOriginalLanguage() {
    return originalLanguage;
  }

  @SuppressWarnings("unused") public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  @SuppressWarnings("unused") public String getOriginalTitle() {
    return originalTitle;
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

  @SuppressWarnings("unused") public void setGenreIds(List<Integer> genreIds) {
    this.genreIds = genreIds;
  }

  @SuppressWarnings("unused") public List<Integer> getGenreIds() {
    return genreIds;
  }

  @SuppressWarnings("unused") public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getPosterPath() {
    return posterPath;
  }

  @SuppressWarnings("unused") public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  @SuppressWarnings("unused") public String getBackdropPath() {
    return backdropPath;
  }

  @SuppressWarnings("unused") public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  @SuppressWarnings("unused") public String getReleaseDate() {
    return releaseDate;
  }

  @SuppressWarnings("unused") public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }

  @SuppressWarnings("unused") public double getVoteAverage() {
    return voteAverage;
  }

  @SuppressWarnings("unused") public void setPopularity(double popularity) {
    this.popularity = popularity;
  }

  @SuppressWarnings("unused") public double getPopularity() {
    return popularity;
  }

  @SuppressWarnings("unused") public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @SuppressWarnings("unused") public void setAdult(boolean adult) {
    this.adult = adult;
  }

  @SuppressWarnings("unused") public boolean isAdult() {
    return adult;
  }

  @SuppressWarnings("unused") public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  @SuppressWarnings("unused") public int getVoteCount() {
    return voteCount;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.overview);
    dest.writeString(this.originalLanguage);
    dest.writeString(this.originalTitle);
    dest.writeByte(this.video ? (byte) 1 : (byte) 0);
    dest.writeString(this.title);
    dest.writeList(this.genreIds);
    dest.writeString(this.posterPath);
    dest.writeString(this.backdropPath);
    dest.writeString(this.releaseDate);
    dest.writeDouble(this.voteAverage);
    dest.writeDouble(this.popularity);
    dest.writeInt(this.id);
    dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
    dest.writeInt(this.voteCount);
  }

  public MovieResultsItem() {
  }

  MovieResultsItem(Parcel in) {
    this.overview = in.readString();
    this.originalLanguage = in.readString();
    this.originalTitle = in.readString();
    this.video = in.readByte() != 0;
    this.title = in.readString();
    this.genreIds = new ArrayList<>();
    in.readList(this.genreIds, Integer.class.getClassLoader());
    this.posterPath = in.readString();
    this.backdropPath = in.readString();
    this.releaseDate = in.readString();
    this.voteAverage = in.readDouble();
    this.popularity = in.readDouble();
    this.id = in.readInt();
    this.adult = in.readByte() != 0;
    this.voteCount = in.readInt();
  }

  public static final Parcelable.Creator<MovieResultsItem> CREATOR = new Parcelable.Creator<MovieResultsItem>() {
    @Override public MovieResultsItem createFromParcel(Parcel source) {
      return new MovieResultsItem(source);
    }

    @Override public MovieResultsItem[] newArray(int size) {
      return new MovieResultsItem[size];
    }
  };
}
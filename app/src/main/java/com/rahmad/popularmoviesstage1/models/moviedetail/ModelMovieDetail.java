package com.rahmad.popularmoviesstage1.models.moviedetail;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelMovieDetail implements Parcelable {
  private String backdropPath;
  private String overview;
  private String originalTitle;
  private String releaseDate;
  private double voteAverage;
  private String posterPath;

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getOverview() {
    return overview;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getPosterPath() {
    return posterPath;
  }

  @Override public String toString() {
    return "ModelMovieDetail{"
        + "backdrop_path = '"
        + backdropPath
        + '\''
        + ",overview = '"
        + overview
        + '\''
        + ",original_title = '"
        + originalTitle
        + '\''
        + ",release_date = '"
        + releaseDate
        + '\''
        + ",vote_average = '"
        + voteAverage
        + '\''
        + ",poster_path = '"
        + posterPath
        + '\''
        + "}";
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.backdropPath);
    dest.writeString(this.overview);
    dest.writeString(this.originalTitle);
    dest.writeString(this.releaseDate);
    dest.writeDouble(this.voteAverage);
    dest.writeString(this.posterPath);
  }

  public ModelMovieDetail() {
  }

  protected ModelMovieDetail(Parcel in) {
    this.backdropPath = in.readString();
    this.overview = in.readString();
    this.originalTitle = in.readString();
    this.releaseDate = in.readString();
    this.voteAverage = in.readDouble();
    this.posterPath = in.readString();
  }

  public static final Parcelable.Creator<ModelMovieDetail> CREATOR = new Parcelable.Creator<ModelMovieDetail>() {
    @Override public ModelMovieDetail createFromParcel(Parcel source) {
      return new ModelMovieDetail(source);
    }

    @Override public ModelMovieDetail[] newArray(int size) {
      return new ModelMovieDetail[size];
    }
  };
}

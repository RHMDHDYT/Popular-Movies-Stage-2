package com.rahmad.popularmoviesstage2.models.movielist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MovieResponse implements Parcelable {

  @SerializedName("page") private int page;

  @SerializedName("total_pages") private int totalPages;

  @SerializedName("results") private List<MovieResultsItem> results;

  @SerializedName("total_results") private int totalResults;

  @SuppressWarnings("unused") public void setPage(int page) {
    this.page = page;
  }

  @SuppressWarnings("unused") public int getPage() {
    return page;
  }

  @SuppressWarnings("unused") public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  @SuppressWarnings("unused") public int getTotalPages() {
    return totalPages;
  }

  @SuppressWarnings("unused") public void setResults(List<MovieResultsItem> results) {
    this.results = results;
  }

  public List<MovieResultsItem> getResults() {

    return results;
  }

  @SuppressWarnings("unused") public void setTotalResults(int totalResults) {
    this.totalResults = totalResults;
  }

  @SuppressWarnings("unused") public int getTotalResults() {
    return totalResults;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.page);
    dest.writeInt(this.totalPages);
    dest.writeList(this.results);
    dest.writeInt(this.totalResults);
  }

  @SuppressWarnings("unused") public MovieResponse() {
  }

  private MovieResponse(Parcel in) {
    this.page = in.readInt();
    this.totalPages = in.readInt();
    this.results = new ArrayList<>();
    in.readList(this.results, MovieResultsItem.class.getClassLoader());
    this.totalResults = in.readInt();
  }

  public static final Parcelable.Creator<MovieResponse> CREATOR = new Parcelable.Creator<MovieResponse>() {
    @Override public MovieResponse createFromParcel(Parcel source) {
      return new MovieResponse(source);
    }

    @Override public MovieResponse[] newArray(int size) {
      return new MovieResponse[size];
    }
  };
}
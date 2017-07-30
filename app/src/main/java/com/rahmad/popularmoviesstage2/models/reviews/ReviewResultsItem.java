package com.rahmad.popularmoviesstage2.models.reviews;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;

public class ReviewResultsItem implements android.os.Parcelable {

  @SerializedName("author")
  private String author;

  @SerializedName("id")
  private String id;

  @SerializedName("content")
  private String content;

  @SerializedName("url")
  private String url;

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthor() {
    return author;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return
        "ReviewResultsItem{" +
            "author = '" + author + '\'' +
            ",id = '" + id + '\'' +
            ",content = '" + content + '\'' +
            ",url = '" + url + '\'' +
            "}";
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.author);
    dest.writeString(this.id);
    dest.writeString(this.content);
    dest.writeString(this.url);
  }

  public ReviewResultsItem() {
  }

  protected ReviewResultsItem(Parcel in) {
    this.author = in.readString();
    this.id = in.readString();
    this.content = in.readString();
    this.url = in.readString();
  }

  public static final Creator<ReviewResultsItem> CREATOR = new Creator<ReviewResultsItem>() {
    @Override
    public ReviewResultsItem createFromParcel(Parcel source) {
      return new ReviewResultsItem(source);
    }

    @Override
    public ReviewResultsItem[] newArray(int size) {
      return new ReviewResultsItem[size];
    }
  };
}
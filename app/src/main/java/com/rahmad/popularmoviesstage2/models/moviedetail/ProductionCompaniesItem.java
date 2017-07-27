package com.rahmad.popularmoviesstage2.models.moviedetail;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused") class ProductionCompaniesItem {

  @SerializedName("name") private String name;

  @SerializedName("id") private int id;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override public String toString() {
    return "ProductionCompaniesItem{" + "name = '" + name + '\'' + ",id = '" + id + '\'' + "}";
  }
}
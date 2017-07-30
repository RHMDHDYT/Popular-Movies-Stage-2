package com.rahmad.popularmoviesstage2.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hartviq on 10/10/16.
 * inbox.rahmad@gmail.com
 * Copyright 2016
 */

public class AppSharedPref {

  private static final String FILENAME = "APP_SHARED_DATA";
  private final String sortingState = "SORTING_STATE";

  private final SharedPreferences sharedPreferences;
  private final SharedPreferences.Editor editor;

  @SuppressLint("CommitPrefEdits")
  public AppSharedPref(Context context) {
    sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public Integer getSortingState() {
    return sharedPreferences.getInt(sortingState, 1);
  }

  public void setSortingState(Integer sortingState) {
    editor.putInt(this.sortingState, sortingState);
    editor.apply();
  }

  @SuppressWarnings("unused")
  public void clear() {
    editor.clear();
    editor.commit();
  }

  @SuppressWarnings("UnusedReturnValue")
  public boolean commit() {
    return editor.commit();
  }
}

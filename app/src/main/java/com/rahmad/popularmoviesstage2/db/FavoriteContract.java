package com.rahmad.popularmoviesstage2.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

  public static final String CONTENT_AUTHORITY = "com.rahmad.popularmoviesstage2.app";

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


  public static final class FavoriteEntry implements BaseColumns {

    public static final String TABLE_FAVORITE = "favorite";

    public static final String ID = "id";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "movie_title";
    public static final String COLUMN_THUMBNAIL = "movie_thumbnail_url";

    // Content uri
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath(TABLE_FAVORITE).build();

    // Cursor of base type directory for multiple entries
    public static final String CONTENT_DIR_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE;
    // Cursor of base type item for single entry
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE;

    // Building URIs on insertion
    public static Uri buildFavoriteUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }
}

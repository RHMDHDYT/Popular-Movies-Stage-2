package com.rahmad.popularmoviesstage2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry;


public class FavoriteDBHelper extends SQLiteOpenHelper {

  public static final String LOG_TAG = FavoriteDBHelper.class.getSimpleName();

  private static final String DATABASE_NAME = "favorite.db";
  private static final int DATABASE_VERSION = 1;

  public FavoriteDBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Create the database query
  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
        FavoriteEntry.TABLE_FAVORITE + "(" + FavoriteEntry.ID +
        " INTEGER PRIMARY KEY AUTOINCREMENT, " + FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
        FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
        FavoriteEntry.COLUMN_THUMBNAIL +
        " TEXT NOT NULL); ";

    sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
  }

  // Upgrade database when version is changed.
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
        newVersion + ". OLD DATA WILL BE DESTROYED");
    // Drop the table
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_FAVORITE);
    sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
        FavoriteEntry.TABLE_FAVORITE + "'");

    // re-create database
    onCreate(sqLiteDatabase);
  }
}

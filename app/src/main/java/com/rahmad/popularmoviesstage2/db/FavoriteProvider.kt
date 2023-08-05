package com.rahmad.popularmoviesstage2.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry;


public class FavoriteProvider extends ContentProvider {

  private static final String LOG_TAG = FavoriteProvider.class.getSimpleName();
  private static final UriMatcher sUriMatcher = buildUriMatcher();
  private FavoriteDBHelper mOpenHelper;

  // Codes for the UriMatcher //////
  private static final int FAVORITE = 100;
  private static final int FAVORITE_WITH_ID = 200;
  ////////

  private static UriMatcher buildUriMatcher() {
    // Build a UriMatcher by adding a specific code to return based on a match
    // It's common to use NO_MATCH as the code for this case.
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = FavoriteContract.CONTENT_AUTHORITY;

    // add a code for each type of URI you want
    matcher.addURI(authority, FavoriteEntry.TABLE_FAVORITE, FAVORITE);
    matcher.addURI(authority, FavoriteEntry.TABLE_FAVORITE + "/#", FAVORITE_WITH_ID);

    return matcher;
  }

  @Override
  public boolean onCreate() {
    mOpenHelper = new FavoriteDBHelper(getContext());

    return true;
  }

  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);

    switch (match) {
      case FAVORITE: {
        return FavoriteContract.FavoriteEntry.CONTENT_DIR_TYPE;
      }
      case FAVORITE_WITH_ID: {
        return FavoriteContract.FavoriteEntry.CONTENT_ITEM_TYPE;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor retCursor;
    switch (sUriMatcher.match(uri)) {
      // All Flavors selected
      case FAVORITE: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FavoriteEntry.TABLE_FAVORITE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        return retCursor;
      }
      // Individual flavor based on Id selected
      case FAVORITE_WITH_ID: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FavoriteContract.FavoriteEntry.TABLE_FAVORITE,
            projection,
            FavoriteEntry.ID + " = ?",
            new String[]{String.valueOf(ContentUris.parseId(uri))},
            null,
            null,
            sortOrder);
        return retCursor;
      }
      default: {
        // By default, we assume a bad URI
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    Uri returnUri;
    switch (sUriMatcher.match(uri)) {
      case FAVORITE: {
        long _id = db.insert(FavoriteContract.FavoriteEntry.TABLE_FAVORITE, null, values);
        // insert unless it is already contained in the database
        if (_id > 0) {
          returnUri = FavoriteContract.FavoriteEntry.buildFavoriteUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into: " + uri);
        }
        break;
      }

      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);

      }
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int numDeleted;
    switch (match) {
      case FAVORITE:
        numDeleted = db.delete(
            FavoriteContract.FavoriteEntry.TABLE_FAVORITE, selection, selectionArgs);
        // reset _ID
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
            FavoriteContract.FavoriteEntry.TABLE_FAVORITE + "'");
        break;
      case FAVORITE_WITH_ID:
        numDeleted = db.delete(FavoriteContract.FavoriteEntry.TABLE_FAVORITE,
            FavoriteEntry.ID + " = ?",
            new String[]{String.valueOf(ContentUris.parseId(uri))});
        // reset _ID
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
            FavoriteContract.FavoriteEntry.TABLE_FAVORITE + "'");

        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    return numDeleted;
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case FAVORITE:
        // allows for multiple transactions
        db.beginTransaction();

        // keep track of successful inserts
        int numInserted = 0;
        try {
          for (ContentValues value : values) {
            if (value == null) {
              throw new IllegalArgumentException("Cannot have null content values");
            }
            long _id = -1;
            try {
              _id = db.insertOrThrow(FavoriteContract.FavoriteEntry.TABLE_FAVORITE,
                  null, value);
            } catch (SQLiteConstraintException e) {
              Log.w(LOG_TAG, "Attempting to insert " +
                  value.getAsInteger(
                      FavoriteEntry.ID)
                  + " but value is already in database.");
            }
            if (_id != -1) {
              numInserted++;
            }
          }
          if (numInserted > 0) {
            // If no errors, declare a successful transaction.
            // database will not populate if this is not called
            db.setTransactionSuccessful();
          }
        } finally {
          // all transactions occur at once
          db.endTransaction();
        }
        if (numInserted > 0) {
          // if there was successful insertion, notify the content resolver that there
          // was a change
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return numInserted;
      default:
        return super.bulkInsert(uri, values);
    }
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    int numUpdated = 0;

    if (contentValues == null) {
      throw new IllegalArgumentException("Cannot have null content values");
    }

    switch (sUriMatcher.match(uri)) {
      case FAVORITE: {
        numUpdated = db.update(FavoriteContract.FavoriteEntry.TABLE_FAVORITE,
            contentValues,
            selection,
            selectionArgs);
        break;
      }
      case FAVORITE_WITH_ID: {
        numUpdated = db.update(FavoriteContract.FavoriteEntry.TABLE_FAVORITE,
            contentValues,
            FavoriteEntry.ID + " = ?",
            new String[]{String.valueOf(ContentUris.parseId(uri))});
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }

    if (numUpdated > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return numUpdated;
  }

}

package com.rahmad.popularmoviesstage2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry

class FavoriteDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Create the database query
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteEntry.TABLE_FAVORITE + "(" + FavoriteEntry.ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_THUMBNAIL +
                " TEXT NOT NULL); "
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE)
    }

    // Upgrade database when version is changed.
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(
            LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ". OLD DATA WILL BE DESTROYED"
        )
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_FAVORITE)
        sqLiteDatabase.execSQL(
            "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                    FavoriteEntry.TABLE_FAVORITE + "'"
        )

        // re-create database
        onCreate(sqLiteDatabase)
    }

    companion object {
        const val LOG_TAG: String = "FavoriteDBHelper"
        private const val DATABASE_NAME = "favorite.db"
        private const val DATABASE_VERSION = 1
    }
}
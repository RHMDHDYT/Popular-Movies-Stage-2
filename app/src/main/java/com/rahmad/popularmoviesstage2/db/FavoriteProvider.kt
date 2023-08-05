package com.rahmad.popularmoviesstage2.db

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry.buildFavoriteUri

class FavoriteProvider : ContentProvider() {
    private var mOpenHelper: FavoriteDBHelper? = null
    override fun onCreate(): Boolean {
        mOpenHelper = FavoriteDBHelper(context)
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                FavoriteEntry.CONTENT_DIR_TYPE
            }

            FAVORITE_WITH_ID -> {
                FavoriteEntry.CONTENT_ITEM_TYPE
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val retCursor: Cursor
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                retCursor = mOpenHelper!!.readableDatabase.query(
                    FavoriteEntry.TABLE_FAVORITE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                retCursor
            }

            FAVORITE_WITH_ID -> {
                retCursor = mOpenHelper!!.readableDatabase.query(
                    FavoriteEntry.TABLE_FAVORITE,
                    projection,
                    FavoriteEntry.ID + " = ?",
                    arrayOf(ContentUris.parseId(uri).toString()),
                    null,
                    null,
                    sortOrder
                )
                retCursor
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = mOpenHelper!!.writableDatabase
        val returnUri: Uri = when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                val _id = db.insert(FavoriteEntry.TABLE_FAVORITE, null, values)
                // insert unless it is already contained in the database
                if (_id > 0) {
                    buildFavoriteUri(_id)
                } else {
                    throw SQLException("Failed to insert row into: $uri")
                }
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mOpenHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        val numDeleted: Int
        when (match) {
            FAVORITE -> {
                numDeleted = db.delete(
                    FavoriteEntry.TABLE_FAVORITE, selection, selectionArgs
                )
                // reset _ID
                db.execSQL(
                    "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                            FavoriteEntry.TABLE_FAVORITE + "'"
                )
            }

            FAVORITE_WITH_ID -> {
                numDeleted = db.delete(
                    FavoriteEntry.TABLE_FAVORITE,
                    FavoriteEntry.ID + " = ?", arrayOf(ContentUris.parseId(uri).toString())
                )
                // reset _ID
                db.execSQL(
                    "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                            FavoriteEntry.TABLE_FAVORITE + "'"
                )
            }

            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        return numDeleted
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val db = mOpenHelper!!.writableDatabase
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                // allows for multiple transactions
                db.beginTransaction()

                // keep track of successful inserts
                var numInserted = 0
                try {
                    for (value in values) {
                        var _id: Long = -1
                        try {
                            _id = db.insertOrThrow(
                                FavoriteEntry.TABLE_FAVORITE,
                                null, value
                            )
                        } catch (e: SQLiteConstraintException) {
                            Log.w(
                                LOG_TAG, "Attempting to insert " +
                                        value.getAsInteger(
                                            FavoriteEntry.ID
                                        )
                                        + " but value is already in database."
                            )
                        }
                        if (_id != -1L) {
                            numInserted++
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful()
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction()
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    context!!.contentResolver.notifyChange(uri, null)
                }
                numInserted
            }

            else -> super.bulkInsert(uri, values)
        }
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db = mOpenHelper!!.writableDatabase
        requireNotNull(contentValues) { "Cannot have null content values" }
        var numUpdated: Int = when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                db.update(
                    FavoriteEntry.TABLE_FAVORITE,
                    contentValues,
                    selection,
                    selectionArgs
                )
            }

            FAVORITE_WITH_ID -> {
                db.update(
                    FavoriteEntry.TABLE_FAVORITE,
                    contentValues,
                    FavoriteEntry.ID + " = ?",
                    arrayOf(ContentUris.parseId(uri).toString())
                )
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }
        if (numUpdated > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return numUpdated
    }

    companion object {
        private val LOG_TAG = FavoriteProvider::class.java.simpleName
        private val sUriMatcher = buildUriMatcher()

        // Codes for the UriMatcher //////
        private const val FAVORITE = 100
        private const val FAVORITE_WITH_ID = 200

        ////////
        private fun buildUriMatcher(): UriMatcher {
            // Build a UriMatcher by adding a specific code to return based on a match
            // It's common to use NO_MATCH as the code for this case.
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = FavoriteContract.CONTENT_AUTHORITY

            // add a code for each type of URI you want
            matcher.addURI(authority, FavoriteEntry.TABLE_FAVORITE, FAVORITE)
            matcher.addURI(authority, FavoriteEntry.TABLE_FAVORITE + "/#", FAVORITE_WITH_ID)
            return matcher
        }
    }
}
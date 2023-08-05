package com.rahmad.popularmoviesstage2.db

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object FavoriteContract {
    const val CONTENT_AUTHORITY = "com.rahmad.popularmoviesstage2.app"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

    object FavoriteEntry : BaseColumns {
        const val TABLE_FAVORITE = "favorite"
        const val ID = "id"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_TITLE = "movie_title"
        const val COLUMN_THUMBNAIL = "movie_thumbnail_url"

        // Content uri
        @JvmField
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAVORITE).build()

        // Cursor of base type directory for multiple entries
        const val CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE

        // Cursor of base type item for single entry
        const val CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE

        // Building URIs on insertion
        @JvmStatic
        fun buildFavoriteUri(id: Long): Uri {
            return ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }
}
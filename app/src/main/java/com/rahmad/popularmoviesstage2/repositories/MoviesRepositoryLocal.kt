package com.rahmad.popularmoviesstage2.repositories

import android.annotation.SuppressLint
import android.content.Context
import com.rahmad.popularmoviesstage2.db.FavoriteContract
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.services.responses.movielist.MovieResultsItem
import com.rahmad.popularmoviesstage2.util.AppSharedPref
import retrofit2.Response

class MoviesRepositoryLocal(private val context: Context, private val appSharedPref: AppSharedPref) : MoviesRepositoryContract {
    override suspend fun getMovies(
        sortBy: String
    ) : Response<MovieResponse> {
        throw UnsupportedOperationException()
    }

    override fun getSortingState(): Int {
        return appSharedPref.getSortingState()
    }

    override fun setSortingState(sortingState: Int) {
        appSharedPref.setSortingState(sortingState)
    }

    @SuppressLint("Range")
    override fun getFavoritedMovies(): List<MovieResultsItem> {
        val cursor = context.contentResolver.query(
            FavoriteContract.FavoriteEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val favoritedMovies: MutableList<MovieResultsItem> = mutableListOf()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val movie = MovieResultsItem(
                        id = cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)),
                        originalTitle = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                        posterPath = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_THUMBNAIL))
                    )
                    favoritedMovies.add(movie)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return favoritedMovies
    }
}
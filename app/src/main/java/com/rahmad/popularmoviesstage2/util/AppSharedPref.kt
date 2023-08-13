package com.rahmad.popularmoviesstage2.util

import android.content.Context
import android.content.SharedPreferences

class AppSharedPref(context: Context) {

    private val FILENAME = "APP_SHARED_DATA"
    private val sortingState = "SORTING_STATE"

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun getSortingState(): Int {
        return sharedPreferences.getInt(sortingState, 1)
    }

    fun setSortingState(sortingState: Int) {
        editor.putInt(this.sortingState, sortingState)
        editor.apply()
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun commit(): Boolean {
        return editor.commit()
    }
}

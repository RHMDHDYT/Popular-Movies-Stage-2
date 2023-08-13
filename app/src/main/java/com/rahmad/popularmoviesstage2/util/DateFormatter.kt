package com.rahmad.popularmoviesstage2.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by rahmad on 7/9/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
object DateFormatter {
    private fun formatDateFromString(toFormat: String, stringDate: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val newDate = format.parse(stringDate)
            format = SimpleDateFormat(toFormat, Locale.getDefault())
            return format.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return stringDate
    }

    fun getYear(value: String): String {
        return formatDateFromString("yyyy", value)
    }

    @Suppress("unused")
    fun getFullDate(value: String): String {
        return formatDateFromString("dd MMM yyyy", value)
    }
}
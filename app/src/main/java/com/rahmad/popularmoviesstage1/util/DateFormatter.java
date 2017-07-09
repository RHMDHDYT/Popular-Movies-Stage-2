package com.rahmad.popularmoviesstage1.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rahmad on 7/9/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */

public class DateFormatter {

  private static String formatingDateFromString(String fromFormat, String toFormat, String stringDate) {

    SimpleDateFormat format = new SimpleDateFormat(fromFormat, Locale.getDefault());
    try {
      Date newDate = format.parse(stringDate);
      format = new SimpleDateFormat(toFormat, Locale.getDefault());
      return format.format(newDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return stringDate;
  }

  public static String getYear(String value) {
    return formatingDateFromString("yyyy-MM-dd", "yyyy", value);
  }

  public static String getFullDate(String value) {
    return formatingDateFromString("yyyy-MM-dd", "dd MMM yyyy", value);
  }
}

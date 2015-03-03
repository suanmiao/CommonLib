package me.suanmiao.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by suanmiao on 14/12/4.
 */
public class DateUtil {
  private DateUtil() {}

  public static final String FORMATE_HH_MM = "HH:mm";
  public static final String FORMATE_MM_DD_HH_MM = "MM.dd.HH:mm";
  public static final String FORMATE_MM_DD_HH_MM_SS = "MM-dd.HH:mm:ss";
  public static final String FORMATE_MM_DD = "MM.dd";

  public static long HOUR_LENGTH = 1000 * 60 * 60;
  public static final long DAY_LENGTH = 1000 * 60 * 60 * 24;

  public enum DATE {
    TODAY,
    YESTERDAY,
    OLDER
  }

  public static DATE getAbstractDate(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    long todayStart = calendar.getTimeInMillis();
    long yesterdayStart = todayStart - 1000 * 60 * 60 * 24;
    if (time >= todayStart) {
      return DATE.TODAY;
    } else if (time >= yesterdayStart) {
      return DATE.YESTERDAY;
    } else {
      return DATE.OLDER;
    }
  }

  public static String getDate(long time, String format) {
    return new SimpleDateFormat(format).format(time);
  }

  private static final DateFormat[] PUBDATE_DATE_FORMATS = {
      new SimpleDateFormat("d' 'MMM' 'yy' 'HH:mm:ss", Locale.US),
      new SimpleDateFormat("d' 'MMM' 'yy' 'HH:mm:ss' 'Z", Locale.US),
      new SimpleDateFormat("d' 'MMM' 'yy' 'HH:mm:ss' 'z", Locale.US)};

  private static final DateFormat[] UPDATE_DATE_FORMATS = {
      new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US),
      new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ssZ", Locale.US),
      new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSSz", Locale.US),
      new SimpleDateFormat("yyyy-MM-dd", Locale.US)};

  private static final String[][] TIMEZONES_REPLACE = { {"MEST", "+0200"}, {"EST", "-0500"},
      {"PST", "-0800"}};

  public static long parseUpdateDate(String dateStr, boolean tryAllFormat) {
    for (DateFormat format : UPDATE_DATE_FORMATS) {
      try {
        Date result = format.parse(dateStr);
        return result.getTime();
      } catch (ParseException ignored) {} // just do nothing
    }

    if (tryAllFormat)
      return parsePubdateDate(dateStr, false);
    else
      return 0;
  }

  public static long parsePubdateDate(String dateStr) {
    dateStr = improveDateString(dateStr);
    return parsePubdateDate(dateStr, true);
  }

  public static long parsePubdateDate(String dateStr, boolean tryAllFormat) {
    for (DateFormat format : PUBDATE_DATE_FORMATS) {
      try {
        Date result = format.parse(dateStr);
        return result.getTime();
      } catch (ParseException ignored) {} // just do nothing
    }

    if (tryAllFormat)
      return parseUpdateDate(dateStr, false);
    else
      return 0;
  }

  public static String improveDateString(String dateStr) {
    // We remove the first part if necessary (the day display)
    int coma = dateStr.indexOf(", ");
    if (coma != -1) {
      dateStr = dateStr.substring(coma + 2);
    }

    dateStr =
        dateStr.replaceAll("([0-9])T([0-9])", "$1 $2").replaceAll("Z$", "").replaceAll("  ", " ")
            .trim(); // fix useless char

    // Replace bad timezones
    for (String[] timezoneReplace : TIMEZONES_REPLACE) {
      dateStr = dateStr.replace(timezoneReplace[0], timezoneReplace[1]);
    }

    return dateStr;
  }

}

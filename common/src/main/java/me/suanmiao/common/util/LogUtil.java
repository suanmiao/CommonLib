package me.suanmiao.common.util;

import android.support.annotation.IntDef;
import android.util.Log;

/**
 * Created by suanmiao on 15/4/2.
 */
public class LogUtil {
  public static final String LOG_TAG = "SUANLOG";
  public static final int D = 1;
  public static final int I = 2;
  public static final int W = 3;
  public static final int E = 4;

  @IntDef({D, E, I, W})
  public @interface LogLevel {};

  public static void logE(Object... values) {
    logInLevel(E,values);
  }

  public static void logD(Object... values) {
    logInLevel(D,values);
  }

  public static void logInLevel(@LogLevel int level, Object... values) {
    String message = "";
    for(Object o : values){
      message += (o + " ");
    }
    switch (level){
      case D:
        Log.d(LOG_TAG, message);
        break;
      case I:
        Log.i(LOG_TAG, message);
        break;
      case W:
        Log.w(LOG_TAG, message);
        break;
      case E:
        Log.e(LOG_TAG, message);
        break;
    }
  }

  public static void log(Object... values) {
    logInLevel(I, values);
  }

}

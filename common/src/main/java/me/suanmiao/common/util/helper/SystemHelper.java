package me.suanmiao.common.util.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import me.suanmiao.common.component.BaseApplication;


/**
 * Created by suanmiao on 14-12-2.
 */
public class SystemHelper {

  private static int screenHeight = -1;
  private static int screenWidth = -1;

  public static int getStatusBarHeight() {
    return Resources.getSystem().getDimensionPixelSize(
        Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
  }

  public static int getNavigationBarHeight() {
    return Resources.getSystem().getDimensionPixelSize(
        Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android"));
  }

  public static int getScreenHeight() {
    if (screenHeight == -1) {
      WindowManager windowManager =
          (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
      Display display = windowManager.getDefaultDisplay();
      DisplayMetrics metrics = new DisplayMetrics();
      display.getMetrics(metrics);
      screenWidth = metrics.widthPixels;
      screenHeight = metrics.heightPixels;
    }
    return screenHeight;
  }

  public static int getScreenWidth() {
    if (screenHeight == -1) {
      WindowManager windowManager =
          (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
      Display display = windowManager.getDefaultDisplay();
      DisplayMetrics metrics = new DisplayMetrics();
      display.getMetrics(metrics);
      screenWidth = metrics.widthPixels;
      screenHeight = metrics.heightPixels;
    }
    return screenWidth;
  }

}

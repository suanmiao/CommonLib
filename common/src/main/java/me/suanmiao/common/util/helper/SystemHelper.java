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

    private static int[] screenSize = null;

    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getNavigationBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public static int[] getScreenSize() {
        if (screenSize != null) {
            return screenSize;
        } else {
            WindowManager windowManager = (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            screenSize = new int[]{width, height};
            return screenSize;
        }
    }

}

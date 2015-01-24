package com.suan.example.util.helper;

import java.io.File;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

/**
 * Helper to do some system behavior.
 *
 * @author xuda@wandoujia.com
 */
public class FileHelper {

    private static final String ROOT_DIR = "reder";

    private static final int RESTORE_SYSTEM_SCREEN_ON_TIMEOUT_DELAY = 15000;

    private static final int DEFAULT_SYSTEM_SCREEN_ON_TIMEOUT = 15000;

    private static int systemScreenTimeout = -1;

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    private FileHelper() {
    }

    /**
     * Get the external storage path of the device
     *
     * @return The external storage path of the device.
     */
    public static String getAppRootDirectory() {
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return null;
            }
        } catch (Exception e) {
            // Catch exception is trying to fix a crash inside of Environment.getExternalStorageState().
            e.printStackTrace();
            return null;
        }
        String rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + ROOT_DIR + "/";
        File file = new File(rootDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return rootDir;
    }

    /**
     * Get the external storage path of the device
     *
     * @return The external storage path of the device.
     */
    public static String getStorageRootDirectory() {
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return null;
            }
        } catch (Exception e) {
            // Catch exception is trying to fix a crash inside of Environment.getExternalStorageState().
            e.printStackTrace();
            return null;
        }
        String rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/";
        File file = new File(rootDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return rootDir;
    }

}

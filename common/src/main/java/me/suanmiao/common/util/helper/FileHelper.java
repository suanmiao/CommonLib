package me.suanmiao.common.util.helper;

import android.os.Environment;

import java.io.File;

/**
 * Helper to do some system behavior.
 *
 */
public class FileHelper {

    private FileHelper() {
    }

    /**
     * Get the external storage path of the device
     *
     * @return The external storage path of the device.
     */
    public static String getAppRootDirectory(String appFolderName) {
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
                .getAbsolutePath() + "/" + appFolderName + "/";
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

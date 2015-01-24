package com.suan.common.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by suanmiao on 14-11-29.
 */
public class FileUtils {

    public static void saveTextFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

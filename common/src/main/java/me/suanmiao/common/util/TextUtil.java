package me.suanmiao.common.util;

import android.graphics.Paint;
import android.text.TextUtils;

/**
 * Created by suanmiao on 14/12/10.
 */
public class TextUtil {

    private TextUtil() {
    }

    public static String removeEmptyCharacters(String text) {
        if (text != null) {
            text = text.replaceAll("\\s*", "");
        } else {
            text = "";
        }
        return text;
    }

    public static String parseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        } else {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
        }
        return url;
    }

    public static float getTextWidth(Paint paint, String text,float textSize) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        paint.setTextSize(textSize);
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float totalWidth = 0;
        for (int i = 0; i < widths.length; i++) {
            totalWidth += widths[i];
        }
        return totalWidth;
    }
}

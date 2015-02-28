package me.suanmiao.common.util;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by suanmiao on 14/12/29.
 */
public class ActivityUtil {
    private ActivityUtil() {
    }

    public static Intent getViewIntent(String url) {
        Intent result = new Intent(Intent.ACTION_VIEW);
        if(!url.startsWith("http")){
            url = "http://"+url;
        }
        result.setData(Uri.parse(url));
        return result;
    }

}

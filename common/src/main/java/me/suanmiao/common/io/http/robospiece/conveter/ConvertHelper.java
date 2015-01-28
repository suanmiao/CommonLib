package me.suanmiao.common.io.http.robospiece.conveter;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suanmiao on 14-11-1.
 */
public class ConvertHelper {

  public static final int GET_RET_NONE = -1;

  public static String getToken(String source) {
    String tokenString = "";
    try {
      String contentString = source;
      String regex = "token=(\\d*)";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(contentString);
      while (matcher.find()) {
        String getToken = matcher.group(1);
        if (getToken != null) {
          tokenString = getToken;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tokenString;
  }

  public static int getRet(JSONObject resultObject) {
    try {
      if (resultObject.get("Ret") != null) {
        return Integer.parseInt("" + resultObject.get("Ret"));
      }
    } catch (Exception e) {
        e.printStackTrace();
    }
    try {
      if (resultObject.get("ret") != null) {
        return Integer.parseInt("" + resultObject.get("ret"));
      }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return GET_RET_NONE;
  }

}

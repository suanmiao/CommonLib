package com.suan.example.util.helper;


import com.suan.common.util.UrlUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suanmiao on 14/12/15.
 */
public class AuthHelper {

    public static class WeiboAuth {

        public static final String APP_KEY = "3751407758";
        public static final String APP_SECRET = "01691e9cb20036a9e8616986e82de5b5";

        public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

        public static final String AUTH_URL = "https://api.weibo.com/oauth2/authorize";

        public static final String GET_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

        public static class User {
            public String access_token;
            public String remind_in;
            public String expires_in;
            public String uid;
        }

        public static String getAuthorizeUrl() {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", APP_KEY);
            parameters.put("response_type", "code");
            parameters.put("redirect_uri", REDIRECT_URL);
            parameters.put("display", "mobile");
            return AUTH_URL + "?" + UrlUtil.encodeUrl(parameters)
                    + "&scope=friendships_groups_read,friendships_groups_write";
        }

        public static Map<String, String> getAuthorizeParam(String code) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", APP_KEY);
            parameters.put("client_secret", APP_SECRET);
            parameters.put("code", code);
            parameters.put("redirect_uri", REDIRECT_URL);
            parameters.put("grant_type", "authorization_code");
            return parameters;
        }

        public static String getCode(String url) {
            String reg = "code=(.*)";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
                return matcher.group(1);
            }
            return "";
        }

        public static String getToken(String result) {
            try {
                JSONObject object = new JSONObject(result);
                return object.get("access_token").toString();
            } catch (Exception e) {

            }
            return "";
        }

    }

    public static class QQAuth {
        public static final String GET_TOKEN_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=[YOUR_APPID]&redirect_uri=[YOUR_REDIRECT_URI]&scope=[THE_SCOPE]";

        public static final String APP_ID = "101169023";
        public static final String APP_KEY = "554897adb265ffe95c20b5248c69b8a4";

        public static final String REDIRECT_URL = "http://jintiankansha.com";
        public static final String SCOPE = "get_user_info,list_album,upload_pic,do_like";

        public static final String GET_OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

        public static class User {
            public String token;
            public String openid;
            public String client_id;
        }

        public static Map<String, String> getGetOpenIDParam(String token) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("access_token", token);
            return parameters;
        }

        public static String getAuthorithationUrl() {
            return "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&scope=" + SCOPE;
        }

        public static String getToken(String input) {
            String reg = "access_token=([^&]*)&";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                return matcher.group(1);
            }
            return "";
        }

        public static User getParsedUser(String input) {
            //callback( {"client_id":"101169023","openid":"457DF8825B7242CCD6D80ED39308B565"} );
            String reg = "client_id[^\\d]*(\\d)*[^\\d]*openid[^\\d]*([^\"]*)";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                User user = new User();
                String client_id = matcher.group(1);
                String openid = matcher.group(2);
                user.client_id = client_id;
                user.openid = openid;
                return user;
            }
            return null;
        }
    }

}

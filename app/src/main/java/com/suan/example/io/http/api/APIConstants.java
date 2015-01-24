package com.suan.example.io.http.api;

/**
 * Created by suanmiao on 14/12/6.
 */
public class APIConstants {

    private APIConstants() {
    }

    public static final String BASE_URL = "http://jintiankansha.com";
    //    public static final String BASE_URL = "http://192.168.31.242:1337";
    public static final String PATH_GET_ARTICLE_LIST = "/mobile/articles";
    public static final String PATH_ARTICLE = "/mobile/article";
    public static final String PATH_GET_CATEGORIES_LIST = "/mobile/categories";

    public static final String PATH_AUTHENTICATION = "/mobile/authentication";

    public static final String PATH_GET_USER = "/mobile/user";

    public static final String PATH_CATEGORY_ACTION = "/mobile/category";

    public static final String PATH_GET_COMMENT_LIST = "/mobile/comments";
    public static final String PATH_COMMENT = "/mobile/comment";

    public static final String PATH_TEST = "/mobile/test";
}

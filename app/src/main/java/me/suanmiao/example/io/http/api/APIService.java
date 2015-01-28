package me.suanmiao.example.io.http.api;

import me.suanmiao.common.io.http.robospiece.api.BaseFormResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by suanmiao on 14/12/6.
 */
public interface APIService {

    @FormUrlEncoded
    @POST(APIConstants.PATH_ARTICLE)
    BaseFormResult articleAction(@Field("token") String token, @Field("id") String id, @Field("action") String action);

    @FormUrlEncoded
    @POST(APIConstants.PATH_CATEGORY_ACTION)
    BaseFormResult categoryAction(@Field("token") String token, @Field("id") String id, @Field("action") String action);

    @FormUrlEncoded
    @POST(APIConstants.PATH_COMMENT)
    BaseFormResult commentAction(@Field("token") String token, @Field("id") String id, @Field("action") String action, @Field("content") String content);

    @FormUrlEncoded
    @POST(APIConstants.PATH_TEST)
    BaseFormResult test(@Field("type") String type);

}

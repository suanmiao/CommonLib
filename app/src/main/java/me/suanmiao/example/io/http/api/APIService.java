package me.suanmiao.example.io.http.api;


import me.suanmiao.example.ui.mvvm.model.ExampleItemModel;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by suanmiao on 14/12/6.
 */
public interface APIService {

  @FormUrlEncoded
  @POST(APIConstants.PATH_GET_EXAMPLE)
  ExampleItemModel getExample(@Field("token") String token, @Field("id") String id);

}

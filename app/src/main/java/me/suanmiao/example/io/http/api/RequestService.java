package me.suanmiao.example.io.http.api;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by suanmiao on 14/12/6.
 */
public class RequestService extends RetrofitGsonSpiceService {

    private static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        if(okHttpClient==null){
            okHttpClient = new OkHttpClient();
        }
        addRetrofitInterface(APIService.class);
    }

    public static OkHttpClient getOkHttpClient() {
        if(okHttpClient==null){
           okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    @Override
    protected String getServerUrl() {
        return APIConstants.BASE_URL;
    }

}

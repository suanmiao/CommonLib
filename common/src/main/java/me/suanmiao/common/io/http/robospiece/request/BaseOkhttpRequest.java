package me.suanmiao.common.io.http.robospiece.request;

import com.octo.android.robospice.request.SpiceRequest;
import com.squareup.okhttp.OkHttpClient;
import me.suanmiao.common.io.http.robospiece.RequestManager;

/**
 * Created by suanmiao on 14/12/15.
 */
public abstract class BaseOkhttpRequest<T> extends SpiceRequest<T>{

    public BaseOkhttpRequest(Class<T> clazz) {
        super(clazz);
    }

    public OkHttpClient getOkHttpClient() {
        return RequestManager.getOkHttpClient();
    }

}

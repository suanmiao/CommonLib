package me.suanmiao.common.io.http.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

/**
 * Created by suanmiao on 15/1/19.
 */
public interface IVolleyActionDelivery<T>{
    public Response<T> parseNetworkResponse(NetworkResponse response);
}

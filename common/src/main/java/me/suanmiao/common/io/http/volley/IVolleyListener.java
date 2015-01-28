package me.suanmiao.common.io.http.volley;

import com.android.volley.Response;

/**
 * Created by suanmiao on 15/1/26.
 */
public interface IVolleyListener<T> extends Response.ErrorListener, Response.Listener<T>{
}

package me.suanmiao.common.io.http;

import android.text.TextUtils;

import com.android.volley.Request;

import java.util.Map;

import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/2/3.
 */
public class VolleyBuilder<T> {
  private int volleyRequestMethod = Request.Method.GET;
  private String url;
  private IVolleyActionDelivery<T> volleyActionDelivery;
  private Map<String, String> headers;
  private Map<String, String> params;

  public VolleyBuilder<T> url(String url) {
    this.url = url;
    return this;
  }

  public VolleyBuilder<T> method(int method) {
    this.volleyRequestMethod = method;
    return this;
  }

  public VolleyBuilder<T> actionDelivery(IVolleyActionDelivery<T> actionDelivery) {
    this.volleyActionDelivery = actionDelivery;
    return this;
  }

  public VolleyBuilder<T> headers(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }

  public VolleyBuilder<T> params(Map<String, String> params) {
    this.params = params;
    return this;
  }

  public CommonRequest<T> build() {
    if (TextUtils.isEmpty(url) || volleyActionDelivery == null) {
      throw new NullPointerException("");
    }
    CommonRequest<T> request = new CommonRequest<T>(volleyRequestMethod, url);
    request.setVolleyHeaders(headers);
    request.setVolleyParams(params);
    request.setVolleyActionDelivery(volleyActionDelivery);
    return request;
  }

}

package me.suanmiao.common.io.http.volley;

import com.android.volley.Request;

import java.util.Map;

import me.suanmiao.common.io.http.image.ICommonRequest;

/**
 * Created by suanmiao on 15/5/1.
 */
public abstract class BaseVolleyRequest<T> implements ICommonRequest {

  private Request<T> volleyRequest;
  private Map<String, String> headers;
  private Map<String, String> params;

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public abstract int getRequestMethod();

  public abstract String getUrl();

  public abstract IVolleyActionDelivery<T> getActionDelivery();

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public void setVolleyRequest(Request<T> volleyRequest) {
    this.volleyRequest = volleyRequest;
  }

  @Override
  public void cancel() {
    if (this.volleyRequest != null) {
      this.volleyRequest.cancel();
    }
  }

}

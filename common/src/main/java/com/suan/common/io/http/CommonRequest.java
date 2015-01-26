package com.suan.common.io.http;

import android.text.TextUtils;

import com.android.volley.Request;
import com.octo.android.robospice.request.SpiceRequest;
import com.suan.common.io.http.image.Photo;
import com.suan.common.io.http.image.spice.PhotoSpiceRequest;
import com.suan.common.io.http.volley.IVolleyActionDelivery;

import java.util.Map;

/**
 * Created by suanmiao on 15/1/18.
 */
public class CommonRequest<T> {

  /**
   * Volley
   */
  private int volleyRequestMethod;
  private String url;
  private IVolleyActionDelivery<T> volleyActionDelivery;
  private Request<T> volleyRequest;
  private Map<String, String> headers;
  private Map<String, String> params;

  /**
   * Robo
   */
  private SpiceRequest<T> spiceRequest;

  /**
   * common
   */
  boolean photoRequest = false;
  private Photo.LoadOption loadOption = Photo.LoadOption.BOTH;

  public enum RequestType {
    ROBO_REQUEST,
    VOLLEY_REQUEST
  }

  private RequestType requestType;

  protected CommonRequest(int volleyRequestMethod, String url, Map<String, String> headers,
      Map<String, String> params,
      IVolleyActionDelivery<T> volleyActionDelivery) {
    this.volleyRequestMethod = volleyRequestMethod;
    this.url = url;
    this.headers = headers;
    this.params = params;
    this.volleyActionDelivery = volleyActionDelivery;
    this.requestType = RequestType.VOLLEY_REQUEST;
  }

  protected CommonRequest(SpiceRequest<T> request) {
    this.spiceRequest = request;
    this.requestType = RequestType.ROBO_REQUEST;
  }

  public boolean isPhotoRequest() {
    return photoRequest;
  }

  public void setIsPhotoRequest(boolean photoRequest) {
    this.photoRequest = photoRequest;
  }

  public Photo.LoadOption getLoadOption() {
    return loadOption;
  }

  public void setLoadOption(Photo.LoadOption loadOption) {
    this.loadOption = loadOption;
  }

  public Map<String, String> getVolleyHeaders() {
    return headers;
  }

  public Map<String, String> getVolleyParams() {
    return params;
  }

  public int getVolleyRequestMethod() {
    return volleyRequestMethod;
  }

  public String getUrl() {
    return url;
  }

  public void setVolleyRequest(Request<T> volleyRequest) {
    this.volleyRequest = volleyRequest;
  }

  public IVolleyActionDelivery<T> getVolleyActionDelivery() {
    return volleyActionDelivery;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public SpiceRequest<T> getSpiceRequest() {
    if (spiceRequest != null && spiceRequest instanceof PhotoSpiceRequest) {
      PhotoSpiceRequest photoSpiceRequest = (PhotoSpiceRequest) spiceRequest;
      photoSpiceRequest.setLoadOption(loadOption);
    }
    return spiceRequest;
  }

  public void cancel() {
    switch (requestType) {
      case VOLLEY_REQUEST:
        if (this.volleyRequest != null) {
          this.volleyRequest.cancel();
        }
        break;
      case ROBO_REQUEST:
        if (this.spiceRequest != null) {
          this.spiceRequest.cancel();
        }
        break;
    }
  }

  public void cancel(boolean interrupt) {
    cancel();
  }

  public static class VolleyBuilder<T> {
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
      return new CommonRequest<T>(volleyRequestMethod, url, headers, params, volleyActionDelivery);
    }
  }

  public static class SpiceBuilder<T> {

    private SpiceRequest<T> spiceRequest;

    public SpiceBuilder<T> request(SpiceRequest<T> request) {
      this.spiceRequest = request;
      return this;
    }

    public CommonRequest<T> build() {
      if (spiceRequest == null) {
        throw new NullPointerException("");
      }
      return new CommonRequest<T>(spiceRequest);
    }
  }

}

package me.suanmiao.common.io.http;

import com.android.volley.Request;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.Map;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.io.http.image.spice.PhotoSpiceRequest;
import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;

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

  protected CommonRequest(int volleyRequestMethod, String url) {
    this.volleyRequestMethod = volleyRequestMethod;
    this.url = url;
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

  public void setVolleyHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void setVolleyParams(Map<String, String> params) {
    this.params = params;
  }

  public void setVolleyActionDelivery(IVolleyActionDelivery<T> volleyActionDelivery) {
    this.volleyActionDelivery = volleyActionDelivery;
  }

  public IVolleyActionDelivery<T> getVolleyActionDelivery() {
    return volleyActionDelivery;
  }

  public void setVolleyRequest(Request<T> volleyRequest) {
    this.volleyRequest = volleyRequest;
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

}

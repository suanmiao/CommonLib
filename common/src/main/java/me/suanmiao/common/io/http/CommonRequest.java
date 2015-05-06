package me.suanmiao.common.io.http;

import com.android.volley.Request;
import com.octo.android.robospice.request.SpiceRequest;

import me.suanmiao.common.io.http.volley.BaseVolleyRequest;

/**
 * Created by suanmiao on 15/1/18.
 */
public class CommonRequest<T> {

  /**
   * Volley
   */
  private BaseVolleyRequest<T> volleyRequest;

  private Request<T> fakeVolleyRequest;

  /**
   * Robo
   */
  private SpiceRequest<T> spiceRequest;

  /**
   * common
   */
  boolean photoRequest = false;

  public enum RequestType {
    ROBO_REQUEST,
    VOLLEY_REQUEST
  }

  private RequestType requestType;

  protected CommonRequest(BaseVolleyRequest<T> request) {
    this.volleyRequest = request;
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

  public void setVolleyRequest(Request<T> volleyRequest) {
    this.fakeVolleyRequest = volleyRequest;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public SpiceRequest<T> getSpiceRequest() {
    return spiceRequest;
  }

  public void cancel() {
    switch (requestType) {
      case VOLLEY_REQUEST:
        if (this.volleyRequest != null) {
          this.fakeVolleyRequest.cancel();
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

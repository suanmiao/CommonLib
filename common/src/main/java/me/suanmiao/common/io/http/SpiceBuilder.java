package me.suanmiao.common.io.http;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * Created by suanmiao on 15/2/3.
 */
public class SpiceBuilder<T> {

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

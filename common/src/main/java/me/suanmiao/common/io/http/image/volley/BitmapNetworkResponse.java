package me.suanmiao.common.io.http.image.volley;

import android.graphics.Bitmap;

import com.android.volley.NetworkResponse;

import java.util.Collections;

/**
 * Created by suanmiao on 15/1/26.
 */
public class BitmapNetworkResponse extends NetworkResponse {
  private Bitmap result;

  public BitmapNetworkResponse(Bitmap result) {
    super(new byte[] {}, Collections.<String, String>emptyMap());
    this.result = result;
  }

  public Bitmap getResult() {
    return result;
  }
}

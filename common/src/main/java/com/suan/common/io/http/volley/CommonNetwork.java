package com.suan.common.io.http.volley;

import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.HttpClientStack;
import com.suan.common.component.BaseApplication;
import com.suan.common.io.cache.CacheManager;
import com.suan.common.io.http.image.Photo;
import com.suan.common.io.http.image.volley.BitmapNetworkResponse;

import java.io.IOException;

/**
 * Created by suanmiao on 15/1/26.
 */
public class CommonNetwork extends BasicNetwork {

  private static int DEFAULT_POOL_SIZE = 4096;

  static String userAgent = "volley/0";

  public CommonNetwork() {
    super(new HttpClientStack(AndroidHttpClient.newInstance(userAgent)), new ByteArrayPool(
        DEFAULT_POOL_SIZE));
  }

  @Override
  public NetworkResponse performRequest(Request<?> request) throws VolleyError {
    if (request instanceof FakeVolleyRequest && ((FakeVolleyRequest) request).isPhotoRequest()) {
      FakeVolleyRequest<Photo> photoFakeVolleyRequest = (FakeVolleyRequest<Photo>) request;
      if (photoFakeVolleyRequest.getLoadOption() == Photo.LoadOption.ONLY_FROM_CACHE
          || photoFakeVolleyRequest.getLoadOption() == Photo.LoadOption.BOTH) {
        Bitmap result = getImage(photoFakeVolleyRequest);
        if (result != null
            || photoFakeVolleyRequest.getLoadOption() == Photo.LoadOption.ONLY_FROM_CACHE) {
          return new BitmapNetworkResponse(result);
        }
      }
    }
    return super.performRequest(request);
  }

  private Bitmap getImage(FakeVolleyRequest fakeVolleyRequest) {
    try {
      CacheManager mCacheManager = BaseApplication.getRequestManager().getCacheManager();
      return mCacheManager.get(fakeVolleyRequest.getUrl());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

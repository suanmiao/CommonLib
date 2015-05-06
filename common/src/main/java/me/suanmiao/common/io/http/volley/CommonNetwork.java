package me.suanmiao.common.io.http.volley;

import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;

import java.io.IOException;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.CacheManager;
import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;
import me.suanmiao.common.io.cache.mmbean.BaseMMBean;
import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.io.http.image.volley.BitmapNetworkResponse;
import me.suanmiao.common.io.http.image.volley.FakePhotoVolleyRequest;

/**
 * Created by suanmiao on 15/1/26.
 */
public class CommonNetwork extends BasicNetwork {

  static String userAgent = "volley/0";

  public CommonNetwork() {
    super(new HttpClientStack(AndroidHttpClient.newInstance(userAgent)));
  }

  @Override
  public NetworkResponse performRequest(Request<?> request) throws VolleyError {
    if (request instanceof FakePhotoVolleyRequest) {
      FakePhotoVolleyRequest photoFakeVolleyRequest = (FakePhotoVolleyRequest) request;
      Photo.Option loadOption = photoFakeVolleyRequest.getPhoto().getLoadOption();
      if (loadOption == null) {
        loadOption = new Photo.Option();
      }
      if (loadOption.loadSource == Photo.LoadSource.ONLY_FROM_CACHE
          || loadOption.loadSource == Photo.LoadSource.BOTH) {
        Bitmap cachedImage = getImageFromCache(photoFakeVolleyRequest);
        if (cachedImage != null
            || loadOption.loadSource == Photo.LoadSource.ONLY_FROM_CACHE) {
          return new BitmapNetworkResponse(cachedImage);
        }
      }
    }
    return super.performRequest(request);
  }

  private Bitmap getImageFromCache(FakePhotoVolleyRequest fakeVolleyRequest) {
    try {
      CacheManager mCacheManager = BaseApplication.getRequestManager().getCacheManager();
      AbstractMMBean bean = mCacheManager.get(fakeVolleyRequest.getPhoto().getCacheKey());
      if (bean != null && bean.getDataType() == AbstractMMBean.TYPE_BITMAP) {
        return ((BaseMMBean) bean).getDataBitmap();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

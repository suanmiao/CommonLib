package com.suan.common.io.http.robospiece;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.octo.android.robospice.SpiceManager;
import com.squareup.okhttp.OkHttpClient;
import com.suan.common.component.BaseApplication;
import com.suan.common.io.cache.CacheManager;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.MRequestListener;
import com.suan.common.io.http.robospiece.api.TaggedRequestListener;
import com.suan.common.io.http.volley.FakeVolleyRequest;
import com.suan.common.util.helper.FileHelper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suanmiao on 14-10-31.
 */
public class RequestManager {

  /**
   * common
   */
  private CacheManager cacheManager;
  private static final String DISK_BITMAP_CACHE_DIR = FileHelper.getAppRootDirectory() + "/cache";
  private static final String DISK_HTTP_CACHE_DIR = FileHelper.getAppRootDirectory() + "/httpCache";

  /**
   * RoboSpiece
   */
  private SpiceManager spiceManager;
  private Map<Object, List<BaseRequest>> runningRequest;
  private static OkHttpClient mOkHttpClient;
  RequestQueue requestQueue;

  /**
   * two specific demands for wace
   * 1.dynamic header for all request, cause token and slave_user is dynamic
   */

  public enum ExecuteMode {
    ROBO_SPIECE,
    VOLLEY,
    BOTH
  }

  private ExecuteMode mExecuteMode;

  public RequestManager(Class requestService, OkHttpClient okHttpClient) {
    initCommon();
    initRobo(requestService, okHttpClient);
  }

  public RequestManager(Context context) {
    initCommon();
    initVolley(context);
    this.mExecuteMode = ExecuteMode.VOLLEY;
  }

  private void initCommon() {
    runningRequest = new HashMap<>();
    cacheManager =
        new CacheManager(DISK_BITMAP_CACHE_DIR, DISK_HTTP_CACHE_DIR,
            BaseApplication.getAppContext());
  }

  private void initRobo(Class requestService, OkHttpClient okHttpClient) {
    runningRequest = new HashMap<>();
    spiceManager = new SpiceManager(requestService);
    mOkHttpClient = okHttpClient;
    spiceManager.start(BaseApplication.getAppContext());
    this.mExecuteMode = ExecuteMode.ROBO_SPIECE;
  }

  private void initVolley(Context context) {
    requestQueue = Volley.newRequestQueue(context);
  }

  public static OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  public void cancelRequest(Object tag) {
    List<BaseRequest> taggedRequestList = runningRequest.get(tag);
    if (taggedRequestList != null) {
      for (BaseRequest request : taggedRequestList) {
        request.cancel();
      }
    }
  }

  protected void addRequestToTagList(Object tag, BaseRequest request) {
    List<BaseRequest> list = runningRequest.get(tag);
    if (list != null) {
      list.add(request);
    } else {
      list = new ArrayList<>();
      list.add(request);
      runningRequest.put(tag, list);
    }
  }

  public void removeRequestFromTagList(Object tag, BaseRequest request) {
    List<BaseRequest> list = runningRequest.get(tag);
    if (list != null) {
      list.remove(request);
    }
  }

  public <T> void executeRequest(BaseRequest<T> request, MRequestListener<T> requestListener,
      Object tag) {
    addRequestToTagList(tag, request);
    switch (request.getRequestType()) {
      case VOLLEY_REQUEST:
        switch (request.getVolleyRequestMethod()) {
          case Request.Method.POST: {
            FakeVolleyRequest<T> volleyRequest =
                new FakeVolleyRequest<T>(request.getVolleyRequestMethod(), request.getUrl(),
                    request.getVolleyHeaders(), request.getVolleyParams(),
                    request.getVolleyActionDelivery(),
                    requestListener.getVolleyListener(), requestListener.getErrorListener());
            requestQueue.add(volleyRequest);
            request.setVolleyRequest(volleyRequest);
            break;
          }
          case Request.Method.GET: {
            FakeVolleyRequest<T> volleyRequest =
                new FakeVolleyRequest<T>(request.getVolleyRequestMethod(), getEncodedGetUrl(
                    request.getUrl(), request.getVolleyParams()),
                    request.getVolleyHeaders(), null,
                    request.getVolleyActionDelivery(),
                    requestListener.getVolleyListener(), requestListener.getErrorListener());
            requestQueue.add(volleyRequest);
            request.setVolleyRequest(volleyRequest);
            break;
          }
        }
        break;
      case ROBO_REQUEST:
        spiceManager.execute(request.getSpiceRequest(), new TaggedRequestListener<T>(tag, request,
            requestListener.getRoboRequestListener(),
            this));
        break;
    }
  }

  private String getEncodedGetUrl(String url, Map<String, String> params) {
    if (TextUtils.isEmpty(url) || params == null) {
      return url;
    }
    ArrayList<Map.Entry<String, String>> entryArrayList = new ArrayList<>();
    entryArrayList.addAll(params.entrySet());
    for (int i = 0; i < entryArrayList.size(); i++) {
      Map.Entry<String, String> entry = entryArrayList.get(i);
      String key = entry.getKey();
      String value = entry.getValue();
      try {
        value = URLEncoder.encode(value, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        key = URLEncoder.encode(key, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (i == 0) {
        url += ("?" + key + "=" + value);
      } else {
        url += ("&" + key + "=" + value);
      }
    }
    return url;
  }
  // public void executeRequest(IRequest request, RequestListener requestListener, Object tag) {
  // addRequestToTagList(tag, request);
  // if (request instanceof SpiceRequest) {
  // SpiceRequest spiceRequest = (SpiceRequest) request;
  // spiceManager.execute(spiceRequest, new TaggedRequestListener(tag, request, requestListener,
  // this));
  // } else if (request instanceof Request) {
  // Request volleyRequest = (Request) request;
  // requestQueue.add(volleyRequest);
  // }
  // }

}

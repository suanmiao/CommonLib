package me.suanmiao.common.io.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.octo.android.robospice.SpiceManager;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.CacheManager;
import me.suanmiao.common.io.http.exception.CommonParamException;
import me.suanmiao.common.io.http.exception.CommonRequestException;
import me.suanmiao.common.io.http.robospiece.api.TaggedRequestListener;
import me.suanmiao.common.io.http.volley.CommonNetwork;
import me.suanmiao.common.io.http.volley.FakeVolleyRequest;
import me.suanmiao.common.util.helper.FileHelper;

/**
 * Created by suanmiao on 14-10-31.
 */
public class RequestManager {
  /**
   * common
   */
  private CacheManager cacheManager;
  private String diskBitmapCacheDir;
  private String diskHTTPCacheDir;

  /**
   * RoboSpice
   */
  private SpiceManager spiceManager;
  private Map<String, CommonRequest> runningRequest;
  private static OkHttpClient mOkHttpClient;
  RequestQueue requestQueue;
  private static final String TAG_PREFIX = "/";

  /**
   * two specific demands for wace
   * 1.dynamic header for all request, cause token and slave_user is dynamic
   */

  public enum ExecuteMode {
    ROBO_SPIECE,
    VOLLEY,
    BOTH
  }

  private static ExecuteMode mExecuteMode;

  public RequestManager(Context context, Class requestService, OkHttpClient okHttpClient,
      String appFolderName) {
    initCommon(appFolderName);
    initVolley(context);
    initRobo(requestService, okHttpClient);
    mExecuteMode = ExecuteMode.BOTH;
  }

  public RequestManager(Class requestService, OkHttpClient okHttpClient, String appFolderName) {
    initCommon(appFolderName);
    initRobo(requestService, okHttpClient);
    mExecuteMode = ExecuteMode.ROBO_SPIECE;
  }

  public RequestManager(Context context, String appFolderName) {
    initCommon(appFolderName);
    initVolley(context);
    mExecuteMode = ExecuteMode.VOLLEY;
  }

  private void initCommon(String appFolderName) {
    diskBitmapCacheDir = FileHelper.getAppRootDirectory(appFolderName) +
        "/cache";
    diskHTTPCacheDir = FileHelper.getAppRootDirectory(appFolderName) +
        "/httpCache";

    runningRequest = new HashMap<>();
    cacheManager =
        new CacheManager(diskBitmapCacheDir,
            BaseApplication.getAppContext());
  }

  private void initRobo(Class requestService, OkHttpClient okHttpClient) {
    runningRequest = new HashMap<>();
    spiceManager = new SpiceManager(requestService);
    mOkHttpClient = okHttpClient;
    spiceManager.start(BaseApplication.getAppContext());
    mExecuteMode = ExecuteMode.ROBO_SPIECE;
  }

  private void initVolley(Context context) {
    requestQueue =
        new RequestQueue(new DiskBasedCache(new File(diskHTTPCacheDir)), new CommonNetwork());
    requestQueue.start();
  }

  public void setVolleyRequestQueue(RequestQueue requestQueue) {
    if (this.requestQueue != null) {
      this.requestQueue.stop();
    }
    this.requestQueue = requestQueue;
    requestQueue.start();
  }

  public static OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  public static ExecuteMode getExecuteMode() {
    return mExecuteMode;
  }

  private RequestFinishListener mRequestFinishListener = new RequestFinishListener() {
    @Override
    public void onFinish(String hashTag) {
      runningRequest.remove(hashTag);
    }
  };

  public void cancelRequest(Object tag) {
    String key = tag.hashCode() + TAG_PREFIX;
    List<Map.Entry<String, CommonRequest>> entryList = new ArrayList<>();
    entryList.addAll(runningRequest.entrySet());
    for (Map.Entry<String, CommonRequest> entry : entryList) {
      if (entry.getKey().contains(key)) {
        entry.getValue().cancel();
        runningRequest.remove(entry.getKey());
      }
    }
  }

  protected void addRequestToTagList(Object tag, CommonRequest request) {
    runningRequest.put(generateHashTag(tag, request), request);
  }

  public void removeRequestFromTagList(Object tag, CommonRequest request) {
    runningRequest.remove(generateHashTag(tag, request));
  }

  public <T> void executeRequest(CommonRequest<T> request,
      CommonRequestListener<T> requestListener,
      Object tag) {
    addRequestToTagList(tag, request);
    switch (request.getRequestType()) {
      case VOLLEY_REQUEST:
        switch (request.getVolleyRequestMethod()) {
          case Request.Method.POST: {
            try {
              TaggedRequestListener<T> taggedRequestListener =
                  new TaggedRequestListener<T>(requestListener.getVolleyListener());
              taggedRequestListener.mark(generateHashTag(tag, request), mRequestFinishListener);
              FakeVolleyRequest<T> volleyRequest =
                  new FakeVolleyRequest<T>(request.getVolleyRequestMethod(), request.getUrl(),
                      request.getVolleyHeaders(), request.getVolleyParams(),
                      request.getVolleyActionDelivery(), taggedRequestListener);
              volleyRequest.setLoadOption(request.getLoadOption());
              volleyRequest.setIsPhotoRequest(request.isPhotoRequest());
              volleyRequest.setBlurResult(request.isBlurResult());
              requestQueue.add(volleyRequest);
              request.setVolleyRequest(volleyRequest);
            } catch (CommonRequestException e) {
              e.printStackTrace();
            }
            break;
          }
          case Request.Method.GET: {
            try {
              TaggedRequestListener<T> taggedRequestListener =
                  new TaggedRequestListener<T>(requestListener.getVolleyListener());
              taggedRequestListener.mark(generateHashTag(tag, request), mRequestFinishListener);
              FakeVolleyRequest<T> volleyRequest =
                  new FakeVolleyRequest<T>(request.getVolleyRequestMethod(), getEncodedGETUrl(
                      request.getUrl(), request.getVolleyParams()),
                      request.getVolleyHeaders(), null,
                      request.getVolleyActionDelivery(),
                      taggedRequestListener);
              volleyRequest.setLoadOption(request.getLoadOption());
              volleyRequest.setIsPhotoRequest(request.isPhotoRequest());
              volleyRequest.setBlurResult(request.isBlurResult());
              request.setVolleyRequest(volleyRequest);
              requestQueue.add(volleyRequest);
            } catch (CommonRequestException e) {
              e.printStackTrace();
            }
            break;
          }
        }
        break;
      case ROBO_REQUEST:
        TaggedRequestListener<T> taggedRequestListener = new TaggedRequestListener<T>(
            requestListener.getRoboRequestListener());
        taggedRequestListener.mark(generateHashTag(tag, request), mRequestFinishListener);
        spiceManager.execute(request.getSpiceRequest(), taggedRequestListener);
        break;
    }
  }

  private String getEncodedGETUrl(String url, Map<String, String> params)
      throws CommonRequestException {
    if (TextUtils.isEmpty(url)) {
      throw new CommonParamException("request url is null");
    }
    if (params == null) {
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

  private <T> List<CommonRequest<T>> getRequestListByTag(Object tag) {
    String key = tag.hashCode() + TAG_PREFIX;
    List<Map.Entry<String, CommonRequest<T>>> entryList = new ArrayList<>();
    List<CommonRequest<T>> result = new ArrayList<>();
    entryList.addAll(entryList);
    for (Map.Entry<String, CommonRequest<T>> entry : entryList) {
      if (entry.getKey().contains(key)) {
        result.add(entry.getValue());
      }
    }
    return result;
  }

  private String generateHashTag(Object tag, CommonRequest request) {
    String tagHash = tag == null ? "" : tag.hashCode() + "";
    return tagHash + TAG_PREFIX + request.hashCode();
  }

  public interface RequestFinishListener {
    public void onFinish(String hashTag);
  }

}

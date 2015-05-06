package me.suanmiao.common.io.http.image;

import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;

import java.io.IOException;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;
import me.suanmiao.common.io.cache.mmbean.BaseMMBean;
import me.suanmiao.common.io.cache.mmbean.BigBitmapBean;
import me.suanmiao.common.io.http.ProgressListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.io.http.SpiceCommonListener;
import me.suanmiao.common.io.http.VolleyCommonListener;
import me.suanmiao.common.io.http.image.spice.PhotoSpiceRequest;
import me.suanmiao.common.io.http.volley.BaseVolleyRequest;
import me.suanmiao.common.io.http.volley.PhotoVolleyRequest;
import me.suanmiao.common.ui.widget.BigDrawable;
import me.suanmiao.common.util.LogUtil;
import me.suanmiao.common.util.TextUtil;

/**
 * Created by suanmiao on 14/12/7.
 */
public class Photo {

  public static final int INVALID_VALUE = -1;

  private int viewWidth = INVALID_VALUE;
  private int viewHeight = INVALID_VALUE;

  private String url;

  private AbstractMMBean content;

  /**
   * about progress
   */
  private ProgressListener progressListener;

  private Option loadOption;

  private int contentLength;

  private static boolean saveTraffic = false;

  public static enum ContentState {
    DONE,
    LOADING,
    NONE
  }

  public static enum LoadSource {
    ONLY_FROM_CACHE,
    ONLY_FROM_NETWORK,
    BOTH
  }

  private ContentState contentState = ContentState.NONE;

  private ICommonRequest request;

  private static RequestManager mRequestManager;

  public Photo(String url, int viewWidth, int viewHeight, Option option) {
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    this.url = url;
    this.loadOption = option;
    mRequestManager = BaseApplication.getRequestManager();
  }

  public int getViewWidth() {
    return viewWidth;
  }

  public int getViewHeight() {
    return viewHeight;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public String getCacheKey() {
    return getUrl() + getLoadOption().cacheSuffix;
  }

  public void setContent(AbstractMMBean content) {
    this.content = content;
  }

  public AbstractMMBean getContent() {
    return content;
  }

  public Option getLoadOption() {
    if (loadOption == null) {
      loadOption = new Option();
    }
    return loadOption;
  }

  public void setLoadOption(Option loadOption) {
    this.loadOption = loadOption;
  }

  public void setProgressListener(ProgressListener progressListener) {
    this.progressListener = progressListener;
  }

  public ProgressListener getProgressListener() {
    return progressListener;
  }

  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  public int getContentLength() {
    return contentLength;
  }

  public ContentState getLoadingState() {
    return contentState;
  }

  public void setContentState(ContentState contentState) {
    this.contentState = contentState;
  }

  public ICommonRequest getRequest() {
    return request;
  }

  public void setRequest(ICommonRequest request) {
    this.request = request;
  }

  /**
   * get Photo object from view or create a new instance
   *
   * @param view the view to load image on
   * @param url target url to request image
   * @return
   */
  public static Photo getObject(ImageView view, String url,Option option) {
    /**
     * scheme :
     * when there is a Photo object in imageView's tag,just compare the url ,if not equal ,cancel
     * old and create a new request
     */
    if (view == null) {
      return null;
    }
    url = TextUtil.parseUrl(url);
    if (view.getTag() != null) {
      Photo result = (Photo) view.getTag();
      if (!TextUtils.equals(url, result.getUrl())) {
        result.setContentState(ContentState.NONE);
        result.setUrl(url);
      }
      return result;
    } else {
      int width = 0, height = 0;
      if (view.getLayoutParams() != null) {
        width = view.getLayoutParams().width > 0 ? view.getLayoutParams().width : 0;
        height = view.getLayoutParams().height > 0 ? view.getLayoutParams().height : 0;
      }
      return new Photo(url, width, height, option);
    }
  }

  public static void loadScrollItemImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState) {
    loadScrollItemImg(imageView, url, defaultResourceID, scrollState, null);
  }

  public static void loadScrollItemImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState, Option option) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);

    final Photo photo = Photo.getObject(imageView, url, option);
    if (photo != null) {
      if (photo.getLoadingState() != ContentState.DONE) {
        photo.loadFromRamCache(mRequestManager, imageView);
        if (saveTraffic()) {
          return;
        }
        // no cache got
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            photo.setContentState(ContentState.LOADING);
            if (saveTraffic) {
              photo.getLoadOption().loadSource = LoadSource.ONLY_FROM_CACHE;
            } else {
              photo.getLoadOption().loadSource = LoadSource.BOTH;
            }

            switch (RequestManager.getExecuteMode()) {
              case ROBO_SPIECE: {
                PhotoSpiceRequest request = new PhotoSpiceRequest(photo);

                photo.setRequest(request);
                executeSpiceRequest(request, photo, imageView);
              }
                break;

              case VOLLEY: {
                PhotoVolleyRequest request = new PhotoVolleyRequest(photo);
                photo.setRequest(request);
                executeVolleyRequest(request, photo, imageView);
              }
                break;
            }
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void loadImg(final ImageView imageView, String url, int defaultResourceID) {
    loadImg(imageView, url, defaultResourceID, null);
  }

  public static void loadImg(final ImageView imageView, String url, int defaultResourceID,
      Option option) {
    final Photo photo = Photo.getObject(imageView, url, option);
    if (photo != null) {
      if (photo.getLoadingState() != ContentState.DONE) {
        photo.loadFromRamCache(mRequestManager, imageView);
        if (photo.getLoadingState() != ContentState.DONE) {
          imageView.setImageResource(defaultResourceID);
          photo.setContentState(ContentState.LOADING);
          if (saveTraffic) {
            photo.getLoadOption().loadSource = LoadSource.ONLY_FROM_CACHE;
          } else {
            photo.getLoadOption().loadSource = LoadSource.BOTH;
          }
          switch (RequestManager.getExecuteMode()) {
            case ROBO_SPIECE: {
              // spiceRequest.setLoadSource(LoadSource.BOTH);
              PhotoSpiceRequest request = new PhotoSpiceRequest(photo);

              photo.setRequest(request);
              executeSpiceRequest(request, photo, imageView);
            }
              break;
            case VOLLEY: {
              PhotoVolleyRequest request = new PhotoVolleyRequest(photo);
              photo.setRequest(request);
              executeVolleyRequest(request, photo, imageView);
            }
              break;
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void reloadImg(final ImageView imageView) {
    if (imageView != null && imageView.getTag() != null) {
      final Photo photo = (Photo) imageView.getTag();
      if (photo.getLoadingState() != ContentState.DONE) {
        photo.setContentState(ContentState.LOADING);
        if (saveTraffic) {
          photo.getLoadOption().loadSource = LoadSource.ONLY_FROM_CACHE;
        } else {
          photo.getLoadOption().loadSource = LoadSource.BOTH;
        }
        switch (RequestManager.getExecuteMode()) {
          case ROBO_SPIECE: {
            // spiceRequest.setLoadSource(LoadSource.BOTH);
            PhotoSpiceRequest request = new PhotoSpiceRequest(photo);

            photo.setRequest(request);
            executeSpiceRequest(request, photo, imageView);
          }
            break;
          case VOLLEY: {
            PhotoVolleyRequest request = new PhotoVolleyRequest(photo);
            photo.setRequest(request);
            executeVolleyRequest(request, photo, imageView);
          }
            break;
        }

        imageView.setTag(photo);
      }
    }
  }

  public static void executeSpiceRequest(SpiceRequest request, final Photo photo,
      final ImageView imageView) {
    LogUtil.logD("start spice photo request ", request);
    mRequestManager.executeRequest(request, new SpiceCommonListener<Photo>() {
      @Override
      public void onRequestFailure(SpiceException spiceException) {
        LogUtil.logD(" spice photo request error ", spiceException);
        photo.setContentState(ContentState.NONE);
      }

      @Override
      public void onRequestSuccess(Photo photo) {
        LogUtil.logD(" spice photo request success ", photo);
        if (photo.getContent() != null) {
          processResult(photo, imageView);
        }
      }
    }, imageView);
  }

  public static void executeVolleyRequest(BaseVolleyRequest request, final Photo photo,
      final ImageView imageView) {
    LogUtil.logD("start volley photo request ", request);
    mRequestManager.executeRequest(request, new VolleyCommonListener<Photo>() {
      @Override
      public void onErrorResponse(VolleyError error) {
        LogUtil.logD("volley photo request error ", error);
        photo.setContentState(ContentState.NONE);
      }

      @Override
      public void onResponse(Photo photo) {
        LogUtil.logD("volley photo request success ", photo);
        if (photo.getContent() != null) {
          processResult(photo, imageView);
        }
      }
    }, imageView);
  }

  private static void processResult(Photo photo, ImageView imageView) {
    photo.setContentState(ContentState.DONE);
    if (photo.getLoadOption().mResultHandler == null) {
      AbstractMMBean content = photo.getContent();

      if (content != null) {
        if (content.getDataType() == AbstractMMBean.TYPE_BITMAP) {
          imageView.setImageBitmap(((BaseMMBean) content).getDataBitmap());
        } else if (content.getDataType() == AbstractMMBean.TYPE_BIG_BITMAP) {
          BigDrawable drawable = new BigDrawable(((BigBitmapBean) content).getData());
          imageView.setImageDrawable(drawable);
        }
      }
    } else {
      photo.getLoadOption().mResultHandler.onResult(photo.getContent(), imageView);
    }
  }

  public void loadFromRamCache(RequestManager requestManager, ImageView imageView) {
    this.contentState = ContentState.NONE;
    try {
      AbstractMMBean result = requestManager.getCacheManager().getFromRam(getCacheKey());
      if (result != null) {
        this.setContent(result);
        processResult(this, imageView);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static class Option {
    public boolean sampleBigBitmap = true;
    /**
     * the bitmap will be sampled to the size of image view, this will save memory
     */
    public boolean sampleToImageSize = false;
    /**
     * the max size for normal bitmap ,if 'sampleBigBitmap' is true and the bitmap original size
     * exceeds the size,it will be saved in normal bitmap
     */
    public int sampledMaxBitmapSize;
    public LoadSource loadSource;
    public String cacheSuffix = "";

    public ResultHandler mResultHandler;

    public Option() {
      this.loadSource = LoadSource.BOTH;
      this.sampleBigBitmap = true;
    }
  }

  public interface ResultHandler {
    public void onResult(AbstractMMBean content, ImageView targetImage);
  }

  private static boolean saveTraffic() {
    return saveTraffic;
  }

  public static void setSaveTraffic(boolean saveTraffic) {
    Photo.saveTraffic = saveTraffic;
  }

}

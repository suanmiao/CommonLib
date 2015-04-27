package me.suanmiao.common.io.http.image;

import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.io.IOException;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;
import me.suanmiao.common.io.cache.mmbean.BaseMMBean;
import me.suanmiao.common.io.cache.mmbean.BigBitmapBean;
import me.suanmiao.common.io.http.CommonRequest;
import me.suanmiao.common.io.http.ProgressListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.io.http.SpiceBuilder;
import me.suanmiao.common.io.http.SpiceCommonListener;
import me.suanmiao.common.io.http.VolleyBuilder;
import me.suanmiao.common.io.http.VolleyCommonListener;
import me.suanmiao.common.io.http.image.spice.PhotoSpiceRequest;
import me.suanmiao.common.io.http.image.volley.PhotoActionDelivery;
import me.suanmiao.common.ui.widget.BigDrawable;
import me.suanmiao.common.util.TextUtil;

/**
 * Created by suanmiao on 14/12/7.
 */
public class Photo {

  public static final String BLUR_SUFFIX = "_blur";
  public static final int INVALID_VALUE = -1;

  private int viewWidth = INVALID_VALUE;
  private int viewHeight = INVALID_VALUE;

  private String url;

  private AbstractMMBean content;

  private ResultHandler mResultHandler;

  /**
   * about progress
   */
  private ProgressListener progressListener;

  private int contentLength;

  private static boolean saveTraffic = false;

  public static enum ContentState {
    DONE,
    LOADING,
    NONE
  }

  public static enum LoadOption {
    ONLY_FROM_CACHE,
    ONLY_FROM_NETWORK,
    BOTH
  }

  private ContentState contentState = ContentState.NONE;

  private CommonRequest request;

  private static RequestManager mRequestManager;

  public Photo(String url, int viewWidth, int viewHeight, ResultHandler resultHandler) {
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    this.url = url;
    this.mResultHandler = resultHandler;
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

  public void setContent(AbstractMMBean content) {
    this.content = content;
  }

  public AbstractMMBean getContent() {
    return content;
  }

  public ResultHandler getResultHandler() {
    return mResultHandler;
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

  public PhotoSpiceRequest newSpiceRequest() {
    return new PhotoSpiceRequest(this);
  }

  public CommonRequest getRequest() {
    return request;
  }

  public void setRequest(CommonRequest request) {
    this.request = request;
  }

  /**
   * get Photo object from view or create a new instance
   *
   * @param view the view to load image on
   * @param url target url to request image
   * @return
   */
  public static Photo getObject(ImageView view, String url, ResultHandler resultHandler) {
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
      return new Photo(url, width, height, resultHandler);
    }
  }

  public static void loadScrollItemImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState) {
    loadScrollItemImg(imageView, url, defaultResourceID, scrollState, null);
  }

  public static void loadScrollItemImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState, ResultHandler resultHandler) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);

    final Photo photo = Photo.getObject(imageView, url, resultHandler);
    if (photo != null) {
      if (photo.getLoadingState() != ContentState.DONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url);
        if (saveTraffic()) {
          return;
        }
        // no cache got
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            photo.setContentState(ContentState.LOADING);
            CommonRequest<Photo> request = null;
            switch (RequestManager.getExecuteMode()) {
              case ROBO_SPIECE:
                PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
                spiceRequest.setLoadOption(LoadOption.BOTH);
                request = new SpiceBuilder<Photo>().request(spiceRequest)
                    .build();
                break;

              case VOLLEY:
                request =
                    new VolleyBuilder<Photo>().url(photo.getUrl())
                        .method(Request.Method.GET)
                        .actionDelivery(new PhotoActionDelivery(photo)).build();
                break;
            }
            if (request != null) {
              if (saveTraffic) {
                request.setLoadOption(LoadOption.ONLY_FROM_CACHE);
              } else {
                request.setLoadOption(LoadOption.BOTH);
              }
              photo.setRequest(request);
              executeFullRequest(request, photo, imageView);
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
      ResultHandler resultHandler) {
    final Photo photo = Photo.getObject(imageView, url, resultHandler);
    if (photo != null) {
      if (photo.getLoadingState() != ContentState.DONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url);
        if (photo.getLoadingState() != ContentState.DONE) {
          imageView.setImageResource(defaultResourceID);
          photo.setContentState(ContentState.LOADING);
          CommonRequest<Photo> request = null;
          switch (RequestManager.getExecuteMode()) {
            case ROBO_SPIECE:
              PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
              request =
                  new SpiceBuilder<Photo>().request(spiceRequest)
                      .build();
              break;

            case VOLLEY:
              request =
                  new VolleyBuilder<Photo>().url(photo.getUrl())
                      .method(Request.Method.GET)
                      .actionDelivery(new PhotoActionDelivery(photo)).build();
              break;
          }
          if (request != null) {
            photo.setRequest(request);
            if (saveTraffic) {
              request.setLoadOption(LoadOption.ONLY_FROM_CACHE);
            } else {
              request.setLoadOption(LoadOption.BOTH);
            }
            executeFullRequest(request, photo, imageView);
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
        CommonRequest<Photo> request = null;
        switch (RequestManager.getExecuteMode()) {
          case ROBO_SPIECE:
            PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
            request = new SpiceBuilder<Photo>().request(spiceRequest)
                .build();
            break;

          case VOLLEY:
            request =
                new VolleyBuilder<Photo>().url(photo.getUrl())
                    .method(Request.Method.GET)
                    .actionDelivery(new PhotoActionDelivery(photo)).build();
            break;
        }
        if (request != null) {
          photo.setRequest(request);
          if (saveTraffic) {
            request.setLoadOption(LoadOption.ONLY_FROM_CACHE);
          } else {
            request.setLoadOption(LoadOption.BOTH);
          }
          executeFullRequest(request, photo, imageView);
        }

        imageView.setTag(photo);
      }
    }
  }

  public static void executeFullRequest(CommonRequest<Photo> request, final Photo photo,
      final ImageView imageView) {
    switch (RequestManager.getExecuteMode()) {
      case ROBO_SPIECE:
        mRequestManager.executeRequest(request, new SpiceCommonListener<Photo>() {
          @Override
          public void onRequestFailure(SpiceException spiceException) {
            photo.setContentState(ContentState.NONE);
          }

          @Override
          public void onRequestSuccess(Photo photo) {
            if (photo.getContent() != null) {
              processResult(photo, imageView);
            }
          }
        }, imageView);

        break;
      case VOLLEY:
        mRequestManager.executeRequest(request, new VolleyCommonListener<Photo>() {
          @Override
          public void onErrorResponse(VolleyError error) {
            photo.setContentState(ContentState.NONE);
          }

          @Override
          public void onResponse(Photo photo) {
            if (photo.getContent() != null) {
              processResult(photo, imageView);
            }
          }
        }, imageView);
        break;
    }
  }

  private static void processResult(Photo photo, ImageView imageView) {
    photo.setContentState(ContentState.DONE);
    if (photo.getResultHandler() == null) {
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
      photo.getResultHandler().onResult(photo.getContent(), imageView);
    }
  }

  public void loadFromRamCache(RequestManager requestManager, ImageView imageView, String url) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);
    this.contentState = ContentState.NONE;
    try {
      AbstractMMBean result = requestManager.getCacheManager().getFromRam(url);
      if (result != null) {
        this.setContent(result);
        processResult(this, imageView);
      }
    } catch (IOException e) {
      e.printStackTrace();
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

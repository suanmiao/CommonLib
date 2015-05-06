package me.suanmiao.common.mvvm.viewmodel;

import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.http.CommonRequestListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.io.http.volley.BaseVolleyRequest;
import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.common.mvvm.model.BaseModel;
import me.suanmiao.common.mvvm.view.BaseView;

/**
 * Created by suanmiao on 14-12-3.
 */
public abstract class BaseViewModel<V extends BaseView, T extends BaseModel> {
  protected RequestManager mRequestManager;

  protected V baseView;

  protected Context mContext;
  private UICallback uiCallback;

  public BaseViewModel(V view, Context context, UICallback uiCallback) {
    this.baseView = view;
    this.mRequestManager = BaseApplication.getRequestManager();
    this.mContext = context;
    this.uiCallback = uiCallback;
  }

  public void notifyUIChange() {
    if (this.uiCallback != null) {
      uiCallback.notifyUIChange();
    }
  }

  public void notifyException(SpiceException exception) {
    if (this.uiCallback != null) {
      uiCallback.notifyException(exception);
    }
  }

  public abstract void bind(int index, T baseModel, int scrollState, float scrollSpeed);

  public abstract void idleReload();

  public V getItemView() {
    return baseView;
  }

  public RequestManager getRequestManager() {
    return mRequestManager;
  }

  public Context getContext() {
    return mContext;
  }

  public UICallback getUiCallback() {
    return uiCallback;
  }

  protected void executeRequest(SpiceRequest request, CommonRequestListener requestListener) {
    mRequestManager.executeRequest(request, requestListener, this);
  }

  protected void executeRequest(BaseVolleyRequest request, CommonRequestListener requestListener) {
    mRequestManager.executeRequest(request, requestListener, this);
  }

  public void cancelRequest() {
    mRequestManager.cancelRequest(this);
  }

}

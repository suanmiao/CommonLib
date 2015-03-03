package me.suanmiao.common.mvvm.viewmodel;

import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.http.CommonRequest;
import me.suanmiao.common.io.http.CommonRequestListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.common.mvvm.model.BaseModel;
import me.suanmiao.common.mvvm.view.BaseView;

/**
 * Created by suanmiao on 14-12-3.
 */
public abstract class BaseViewModel {
  protected RequestManager mRequestManager;

  protected BaseView baseView;

  protected Context mContext;
  private UICallback uiCallback;

  public BaseViewModel(BaseView view, Context context, UICallback uiCallback) {
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

  public abstract void bind(int index, BaseModel baseModel, int scrollState, float scrollSpeed);

  public abstract void idleReload();

  public BaseView getView() {
    return baseView;
  }

  public <T> T getView(Class<T> t) {
    return (T) baseView;
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

  public void executeRequest(CommonRequest request, CommonRequestListener listener) {
    mRequestManager.executeRequest(request, listener, this);
  }

  public void cancelRequest() {
    mRequestManager.cancelRequest(this);
  }

}

package me.suanmiao.common.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.request.SpiceRequest;

import butterknife.ButterKnife;
import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.http.CommonRequestListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.io.http.volley.BaseVolleyRequest;

/**
 * Created by suanmiao on 14-12-2.
 */
public abstract class AbstractBaseFragment extends Fragment {

  View contentView;
  protected RequestManager mRequestManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (contentView == null) {
      contentView = inflater.inflate(getLayoutId(), container, false);
      if (contentView != null) {
        ButterKnife.inject(this, contentView);
        afterInjected(contentView, savedInstanceState);
      }
    }
    if (contentView != null && contentView.getParent() != null
        && contentView.getParent() instanceof ViewGroup) {
      ((ViewGroup) contentView.getParent()).removeView(contentView);
    }
    return contentView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    onPrepareLoading();
    contentView.post(new Runnable() {
        @Override
        public void run() {
            onStartLoading();
        }
    });
  }

  public abstract int getLayoutId();

  public abstract void afterInjected(View contentView, Bundle savedInstanceState);

  protected abstract void onPrepareLoading();

  protected abstract void onStartLoading();

  protected void executeRequest(SpiceRequest request, CommonRequestListener requestListener) {
    getRequestManager().executeRequest(request, requestListener, this);
  }

  protected void executeRequest(BaseVolleyRequest request, CommonRequestListener requestListener) {
    getRequestManager().executeRequest(request, requestListener, this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getRequestManager().cancelRequest(this);
  }

  protected RequestManager getRequestManager() {
    if (mRequestManager == null) {
      mRequestManager = BaseApplication.getRequestManager();
    }
    return mRequestManager;
  }
}

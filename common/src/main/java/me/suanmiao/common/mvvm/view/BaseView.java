package me.suanmiao.common.mvvm.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by suanmiao on 14-12-3.
 */
public abstract class BaseView {

  private View contentView;
  private Context mContext;

  public BaseView(Context context, ViewGroup container) {
    this.mContext = context;
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    contentView = inflater.inflate(getLayoutId(), container, false);
    ButterKnife.inject(this, contentView);
    afterInjected();
  }

  public View getContentView() {
    return contentView;
  }

  public abstract int getLayoutId();

  public abstract void afterInjected();

  public <T> T findViewById(int id, Class<T> classOfT) {
    return (T) contentView.findViewById(id);
  }

  public Context getContext() {
    return mContext;
  }

}

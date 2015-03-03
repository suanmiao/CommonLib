package me.suanmiao.common.mvvm.view;

import android.content.Context;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by suanmiao on 15/3/3.
 */
public abstract class BaseInjectView extends BaseView {

  public BaseInjectView(Context context, ViewGroup container) {
    super(context, container);
    ButterKnife.inject(this, getContentView());
    afterInjected();
  }

  @Override
  public void findView() {}

  public abstract void afterInjected();
}

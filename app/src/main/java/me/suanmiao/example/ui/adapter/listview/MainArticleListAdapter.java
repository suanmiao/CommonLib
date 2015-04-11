package me.suanmiao.example.ui.adapter.listview;

import android.content.Context;
import android.view.ViewGroup;

import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;
import me.suanmiao.example.ui.adapter.pager.BaseArticleListAdapter;
import me.suanmiao.example.ui.mvvm.viewmodel.MainArticleNormalItemViewModel;

/**
 * Created by suanmiao on 14/12/3.
 */
public class MainArticleListAdapter extends BaseArticleListAdapter {

  public MainArticleListAdapter(Context context) {
    super(context);
  }

  @Override
  public BaseViewModel newViewModel(int i, ViewGroup container) {
    return new MainArticleNormalItemViewModel(getContext(), container, this, getUICallback());
  }

}

package me.suan.example.ui.adapter.listview;

import android.content.Context;
import android.view.ViewGroup;

import me.suan.common.ui.mvc.ViewModel.BaseViewModel;
import me.suan.example.ui.adapter.pager.BaseArticleListAdapter;
import me.suan.example.ui.mvc.ViewModel.MainArticleNormalItemViewModel;

/**
 * Created by suanmiao on 14/12/3.
 */
public class MainArticleListAdapter extends BaseArticleListAdapter {

  public MainArticleListAdapter(Context context) {
    super(context);
  }

  @Override
  public BaseViewModel newViewModel(int i, ViewGroup container) {
    return new MainArticleNormalItemViewModel(getContext(), container, this, getUIChangeListener());
  }

}

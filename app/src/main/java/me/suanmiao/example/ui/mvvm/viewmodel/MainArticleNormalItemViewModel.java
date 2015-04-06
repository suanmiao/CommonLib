package me.suanmiao.example.ui.mvvm.viewmodel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;
import me.suanmiao.example.ui.adapter.pager.BaseArticleListAdapter;
import me.suanmiao.example.ui.mvvm.model.ArticleModel;
import me.suanmiao.example.ui.mvvm.view.MainArticleNormalItemView;

/**
 * Created by suanmiao on 14-12-3.
 */
public class MainArticleNormalItemViewModel extends BaseViewModel<MainArticleNormalItemView,ArticleModel> {

  private BaseArticleListAdapter listAdapter;

  public MainArticleNormalItemViewModel(Context context, ViewGroup container,
      BaseArticleListAdapter listAdapter, UICallback uiCallback) {
    super(new MainArticleNormalItemView(context, container), context, uiCallback);
    this.listAdapter = listAdapter;
  }

  @Override
  public void bind(final int index, ArticleModel model, int scrollState, float scrollSpeed) {
    getItemView().getContentView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {}
    });
  }

  @Override
  public void idleReload() {
    Photo.reloadImg(getItemView().titleImg);
  }
}

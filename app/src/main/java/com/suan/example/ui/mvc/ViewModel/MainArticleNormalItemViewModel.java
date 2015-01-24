package com.suan.example.ui.mvc.ViewModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.suan.common.io.http.image.Photo;
import com.suan.common.ui.mvc.Model.BaseModel;
import com.suan.common.ui.mvc.ViewModel.BaseViewModel;
import com.suan.example.ui.adapter.pager.BaseArticleListAdapter;
import com.suan.example.ui.mvc.Model.ArticleModel;
import com.suan.example.ui.mvc.View.MainArticleNormalItemView;

/**
 * Created by suanmiao on 14-12-3.
 */
public class MainArticleNormalItemViewModel extends BaseViewModel {

  private BaseArticleListAdapter listAdapter;

  public MainArticleNormalItemViewModel(Context context, ViewGroup container,
      BaseArticleListAdapter listAdapter, UIChangeListener uiChangeListener) {
    super(new MainArticleNormalItemView(context, container), context, uiChangeListener);
    this.listAdapter = listAdapter;
  }

  @Override
  public void bind(final int index, BaseModel baseModel, int scrollState, float scrollSpeed) {
    final ArticleModel model = (ArticleModel) baseModel;
    final MainArticleNormalItemView itemView = (MainArticleNormalItemView) this.baseView;
    itemView.getContentView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {}
    });
  }

  @Override
  public void idleReload() {
    MainArticleNormalItemView itemView = (MainArticleNormalItemView) this.baseView;
    Photo.reloadImg(itemView.titleImg);
  }
}

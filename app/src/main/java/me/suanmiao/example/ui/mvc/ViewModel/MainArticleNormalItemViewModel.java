package me.suanmiao.example.ui.mvc.ViewModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.ui.mvc.Model.BaseModel;
import me.suanmiao.common.ui.mvc.ViewModel.BaseViewModel;
import me.suanmiao.example.ui.adapter.pager.BaseArticleListAdapter;
import me.suanmiao.example.ui.mvc.Model.ArticleModel;
import me.suanmiao.example.ui.mvc.View.MainArticleNormalItemView;

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

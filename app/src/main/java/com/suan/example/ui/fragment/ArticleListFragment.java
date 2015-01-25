package com.suan.example.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.squareup.otto.Subscribe;
import com.suan.common.io.http.CommonRequestListener;
import com.suan.common.io.http.robospiece.api.BaseFormResult;
import com.suan.common.ui.adapter.listview.BaseListAdapter;
import com.suan.common.ui.fragment.AbstractBaseFragment;
import com.suan.common.ui.mvc.ViewModel.BaseViewModel;
import com.suan.common.util.DateUtil;
import com.suan.example.R;
import com.suan.example.event.BusProvider;
import com.suan.example.event.UserStateChangeEvent;
import com.suan.example.io.http.requests.ChannelRequest;
import com.suan.example.ui.adapter.listview.MainArticleListAdapter;
import com.suan.example.ui.mvc.Model.ArticleModel;
import com.suan.example.ui.mvc.Model.ChannelModel;
import com.suan.example.ui.widget.TipView;
import com.suan.ptrListview.PtrListview;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by suanmiao on 14-12-2.
 */
public class ArticleListFragment extends AbstractBaseFragment {

  @InjectView(R.id.list_main_article)
  PtrListview listMainArticle;

  @InjectView(R.id.tip_main_article)
  TipView tipView;

  private MainArticleListAdapter listAdapter;
  private static final int ITEM_COUNT_PER_REQUEST = 20;

  @Override
  public int getLayoutId() {
    return R.layout.fragment_article_list;
  }

  @Override
  public void afterInjected(View contentView, Bundle savedInstanceState) {
    initWidget();
  }

  private void initWidget() {
    listAdapter = new MainArticleListAdapter(getActivity());
    listAdapter.setParser(listParser);
    listAdapter.setUIChangeListener(mUiChangeListener);
    listMainArticle.setonRefreshListener(refreshListener);
    listMainArticle.setOnLoadListener(loadListener);
    listMainArticle.setOnScrollListener(listAdapter);
    listMainArticle.setAdapter(listAdapter);
  }

  private BaseViewModel.UIChangeListener mUiChangeListener = new BaseViewModel.UIChangeListener() {
    @Override
    public void notifyUIChange() {
      listAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyException(SpiceException exception) {

    }

    @Override
    public void notifyStatus(BaseFormResult baseFormResult) {

    }
  };


  private BaseListAdapter.ListParser<ArticleModel> listParser =
      new BaseListAdapter.ListParser<ArticleModel>() {
        @Override
        public List<ArticleModel> parseData(List<ArticleModel> dataList) {
          return dataList;
        }
      };

  private PtrListview.OnRefreshListener refreshListener = new PtrListview.OnRefreshListener() {
    @Override
    public void onRefresh() {
      refreshData();
    }
  };

  private PtrListview.OnLoadListener loadListener = new PtrListview.OnLoadListener() {
    @Override
    public void onLastVisibleItem() {}
  };

  private void refreshData() {
    listMainArticle.onRefreshStart();
    String url = "http://zhihurss.miantiao.me/zhihuzhuanlan/taosay";
    ChannelRequest request = new ChannelRequest(url);
    executeRequest(request, new CommonRequestListener(
        new CommonRequestListener.VolleyListener<ChannelModel>() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.e("SUAN", "response error " + error);
          }

          @Override
          public void onResponse(ChannelModel response) {
            Log.e("SUAN", "response success " + response + "|" + response.title + "|"
                + response.itemList);
            for (ArticleModel articleModel : response.itemList) {
              Log.e("SUAN", "article " + articleModel + "|" + articleModel.title + "|"
                  + articleModel.pubDate + "|" + DateUtil.parsePubdateDate(articleModel.pubDate));
            }
          }
        }));
  }


  private void loadData() {
    listMainArticle.onLoadStart();
  }

  @Override
  protected void onPrepareLoading() {}

  @Override
  protected void onStartLoading() {
    refreshData();
  }

  @Subscribe
  public void onLoginSuccess(UserStateChangeEvent event) {
    refreshData();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    BusProvider.getInstance().register(this);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    BusProvider.getInstance().unregister(this);
  }

}

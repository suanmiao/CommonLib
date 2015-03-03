package me.suanmiao.example.ui.adapter.listview;

import android.content.Context;
import android.view.ViewGroup;

import me.suanmiao.common.ui.adapter.listview.BaseListAdapter;
import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;
import me.suanmiao.example.ui.mvvm.model.ExampleItemModel;
import me.suanmiao.example.ui.mvvm.viewmodel.ExampleItemViewModel;

/**
 * Created by suanmiao on 15/1/26.
 */
public class ExampleListAdapter extends BaseListAdapter<ExampleItemModel> {
  public ExampleListAdapter(Context context) {
    super(context);
  }

  @Override
  public BaseViewModel newViewModel(int i, ViewGroup container) {
    return new ExampleItemViewModel(container,getContext(),getUIChangeListener());
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }
}

package me.suan.example.ui.adapter.listview;

import android.content.Context;
import android.view.ViewGroup;

import me.suan.common.ui.adapter.listview.BaseListAdapter;
import me.suan.common.ui.mvc.ViewModel.BaseViewModel;
import me.suan.example.ui.mvc.Model.ExampleItemModel;
import me.suan.example.ui.mvc.ViewModel.ExampleItemViewModel;

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

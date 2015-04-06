package me.suanmiao.example.ui.mvvm.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import me.suanmiao.common.mvvm.view.BaseView;
import me.suanmiao.example.R;

/**
 * Created by suanmiao on 15/1/26.
 */
public class ExampleItemView extends BaseView {

  @InjectView(R.id.img_main_item)
  public ImageView img;
  @InjectView(R.id.text_item_main_item_title)
  public TextView text;

  public ExampleItemView(Context context, ViewGroup container) {
    super(context, container);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_main_item_normal;
  }

  @Override
  public void afterInjected() {}
}

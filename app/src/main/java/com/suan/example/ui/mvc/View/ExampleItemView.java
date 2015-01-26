package com.suan.example.ui.mvc.View;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suan.common.ui.mvc.View.BaseView;
import com.suan.example.R;

/**
 * Created by suanmiao on 15/1/26.
 */
public class ExampleItemView extends BaseView {

  public ImageView img;
  public TextView text;

  public ExampleItemView(Context context, ViewGroup contanier) {
    super(context, contanier);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_main_item_normal;
  }

  @Override
  public void findView() {
    img = findViewById(R.id.img_main_item, ImageView.class);
    text = findViewById(R.id.text_item_main_item_title, TextView.class);
  }
}

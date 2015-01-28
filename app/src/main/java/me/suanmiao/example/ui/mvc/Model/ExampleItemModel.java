package me.suanmiao.example.ui.mvc.Model;

import me.suanmiao.common.ui.mvc.Model.BaseModel;

/**
 * Created by suanmiao on 15/1/26.
 */
public class ExampleItemModel extends BaseModel {

  public final String img;
  public final String text;

  public ExampleItemModel(String img, String text) {
    this.img = img;
    this.text = text;
  }

  @Override
  public int getViewType() {
    return 0;
  }
}

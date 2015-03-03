package me.suanmiao.example.ui.mvvm.viewmodel;

import android.content.Context;
import android.view.ViewGroup;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.common.mvvm.model.BaseModel;
import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;
import me.suanmiao.example.R;
import me.suanmiao.example.ui.mvvm.model.ExampleItemModel;
import me.suanmiao.example.ui.mvvm.view.ExampleItemView;

/**
 * Created by suanmiao on 15/1/26.
 */
public class ExampleItemViewModel extends BaseViewModel {
  public ExampleItemViewModel(ViewGroup container, Context context,
      UICallback uiCallback) {
    super(new ExampleItemView(context, container), context, uiCallback);
  }

  @Override
  public void bind(int index, BaseModel baseModel, int scrollState, float scrollSpeed) {
    ExampleItemView itemView = (ExampleItemView) getView();
    ExampleItemModel model = (ExampleItemModel) baseModel;
    itemView.text.setText(model.text);
    if (Math.random() > 0.5) {
      Photo.loadScrollItemImg(itemView.img, model.img, R.drawable.ic_launcher, scrollState,
              scrollSpeed);
    } else {
      Photo.loadScrollItemBlurImg(itemView.img, model.img, R.drawable.ic_launcher, scrollState,
          scrollSpeed);
    }
  }

  @Override
  public void idleReload() {
    ExampleItemView itemView = (ExampleItemView) getView();
    Photo.reloadImg(itemView.img);
  }
}

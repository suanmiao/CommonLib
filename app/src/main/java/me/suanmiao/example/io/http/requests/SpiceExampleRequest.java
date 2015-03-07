package me.suanmiao.example.io.http.requests;

import me.suanmiao.common.io.http.robospiece.request.BaseRoboRequest;
import me.suanmiao.example.io.http.api.APIService;
import me.suanmiao.example.ui.mvvm.model.ExampleItemModel;

/**
 * Created by suanmiao on 15/3/7.
 */
public class SpiceExampleRequest extends BaseRoboRequest<ExampleItemModel, APIService> {
  public SpiceExampleRequest() {
    super(ExampleItemModel.class, APIService.class);
  }

  @Override
  public ExampleItemModel loadDataFromNetwork() throws Exception {
    return getService().getExample("token", "id");
  }
}

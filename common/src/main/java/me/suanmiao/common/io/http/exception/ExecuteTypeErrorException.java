package me.suanmiao.common.io.http.exception;

import me.suanmiao.common.io.http.RequestManager;

/**
 * Created by suanmiao on 15/1/24.
 */
public class ExecuteTypeErrorException extends CommonRequestException {

  public ExecuteTypeErrorException(RequestManager.ExecuteMode shouldbe,
      RequestManager.ExecuteMode actually) {
    super("should be "+shouldbe.name()+" mode but actually is "+actually.name());
  }

}

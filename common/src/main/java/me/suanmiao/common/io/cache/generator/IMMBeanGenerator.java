package me.suanmiao.common.io.cache.generator;

import java.io.InputStream;

import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;
import me.suanmiao.common.io.http.image.Photo;

/**
 * Created by suanmiao on 15/4/23.
 */
public interface IMMBeanGenerator {

  public AbstractMMBean generateMMBeanFromTotalStream(InputStream stream);

  public AbstractMMBean constructMMBeanFromNetworkStream(Photo loadOption,InputStream stream);

  public AbstractMMBean constructMMBeanFromNetworkData(Photo loadOption, byte[] data);

}

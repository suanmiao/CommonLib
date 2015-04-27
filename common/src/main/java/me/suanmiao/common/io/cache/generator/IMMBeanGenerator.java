package me.suanmiao.common.io.cache.generator;

import java.io.InputStream;

import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;

/**
 * Created by suanmiao on 15/4/23.
 */
public interface IMMBeanGenerator {

  public AbstractMMBean generateMMBeanFromTotalStream(InputStream stream);

  public AbstractMMBean constructMMBeanFromNetworkStream(InputStream stream);

  public AbstractMMBean constructMMBeanFromNetworkData(byte[] data);

}

package me.suanmiao.common.io.cache.mmbean;

import java.io.OutputStream;

import me.suanmiao.common.ui.widget.BigBitmap;

/**
 * Created by suanmiao on 15/4/24.
 */
public class BigBitmapBean extends AbstractMMBean {
  private BigBitmap mData;

  public BigBitmapBean(BigBitmap bitmap) {
    this.mData = bitmap;
    this.dataType = TYPE_BIG_BITMAP;
  }

  public BigBitmap getData() {
    return mData;
  }

  @Override
  public void writeData(OutputStream stream) {
    this.mData.toStream(stream);
  }

  @Override
  public int getSize() {
    return mData.getTotalSize();
  }
}

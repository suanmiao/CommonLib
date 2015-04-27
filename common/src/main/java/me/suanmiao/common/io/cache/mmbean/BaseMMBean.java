package me.suanmiao.common.io.cache.mmbean;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by suanmiao on 15/4/23.
 * simple multimedia bean to hold byte array and bitmap
 */
public class BaseMMBean extends AbstractMMBean {

  // original data
  private byte[] data;
  // bitmap data
  private Bitmap dataBitmap;

  private static final int BUFFER_SIZE = 512;

  public BaseMMBean(Bitmap bitmap) {
    this.dataType = TYPE_BITMAP;
    this.dataBitmap = bitmap;
  }

  public BaseMMBean(byte[] data) {
    this.dataType = TYPE_BYTE;
    this.data = data;
  }

  @Override
  public void writeData(OutputStream stream) {
    try {
      switch (getDataType()) {
        case TYPE_BYTE:
          stream.write(data);
          break;
        case TYPE_BITMAP:
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          dataBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
          stream.write(byteArrayOutputStream.toByteArray());
          byteArrayOutputStream.close();
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Bitmap getDataBitmap() {
    return dataBitmap;
  }

  public byte[] getData() {
    return data;
  }

  @Override
  public int getSize() {
    switch (getDataType()) {
      case TYPE_BYTE:
        return data.length;
      case TYPE_BITMAP:
        return dataBitmap.getByteCount();
    }
    return 0;
  }
}

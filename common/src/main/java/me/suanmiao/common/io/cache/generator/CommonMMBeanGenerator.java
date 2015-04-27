package me.suanmiao.common.io.cache.generator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;
import me.suanmiao.common.io.cache.mmbean.BaseMMBean;
import me.suanmiao.common.io.cache.mmbean.BigBitmapBean;
import me.suanmiao.common.ui.widget.BigBitmap;

/**
 * Created by suanmiao on 15/4/23.
 */
public class CommonMMBeanGenerator implements IMMBeanGenerator {
  public static final int MAX_NORMAL_BITMAP_SIZE = 500;

  private static final int BUFFER_SIZE = 512;

  @Override
  public AbstractMMBean generateMMBeanFromTotalStream(InputStream stream) {
    try {
      byte[] typeBytes = new byte[AbstractMMBean.LENGTH_TYPE_BYTE];
      stream.read(typeBytes);
      ByteBuffer byteBuffer = ByteBuffer.wrap(typeBytes);
      int dataType = byteBuffer.getInt();
      byte[] sizeBytes = new byte[AbstractMMBean.LENGTH_SIZE_BYTE];
      stream.read(sizeBytes);
      byteBuffer = ByteBuffer.wrap(sizeBytes);
      long dataSize = byteBuffer.getLong();

      switch (dataType) {
        case AbstractMMBean.TYPE_BYTE:
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] buffer = new byte[BUFFER_SIZE];
          int len;
          while ((len = stream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
          }
          baos.flush();
          BaseMMBean result = new BaseMMBean(baos.toByteArray());
          baos.close();
          return result;
        case AbstractMMBean.TYPE_BITMAP:
          return new BaseMMBean(BitmapFactory.decodeStream(stream));
        case AbstractMMBean.TYPE_BIG_BITMAP:
          BigBitmap bigBitmap = BigBitmap.fromStream(stream);
          return new BigBitmapBean(bigBitmap);
      }
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        stream.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public AbstractMMBean constructMMBeanFromNetworkStream(InputStream stream) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int len;
      while ((len = stream.read(buffer)) > -1) {
        baos.write(buffer, 0, len);
      }
      baos.flush();
      byte[] data = baos.toByteArray();
      baos.close();
      return getMMBeanFromByteArray(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new BaseMMBean(BitmapFactory.decodeStream(stream));
  }

  @Override
  public AbstractMMBean constructMMBeanFromNetworkData(byte[] data) {
    return getMMBeanFromByteArray(data);
  }

  private AbstractMMBean getMMBeanFromByteArray(byte[] data) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(data, 0, data.length, options);
    int sourceWidth = options.outWidth;
    int sourceHeight = options.outHeight;
    if (sourceWidth > MAX_NORMAL_BITMAP_SIZE || sourceHeight > MAX_NORMAL_BITMAP_SIZE) {
      BigBitmap bigBitmap = new BigBitmap(data);
      return new BigBitmapBean(bigBitmap);
    } else {
      Bitmap resultBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
      data = null;
      return new BaseMMBean(resultBitmap);
    }
  }

}

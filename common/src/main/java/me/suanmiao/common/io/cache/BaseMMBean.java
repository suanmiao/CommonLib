package me.suanmiao.common.io.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by suanmiao on 15/4/22.
 */
public class BaseMMBean {
  public static final int TYPE_NONE = -1;
  public static final int TYPE_BITMAP = 1;
  public static final int TYPE_BYTE = 2;

  private static final int LENGTH_TYPE_BYTE = 4;
  private static final int LENGTH_SIZE_BYTE = 16;

  private static final int BUFFER_SIZE = 512;

  protected int dataType;
  protected long dataSize;
  // original data
  private byte[] data;
  // bitmap data
  private Bitmap dataBitmap;

  public static BaseMMBean fromBitmapStream(InputStream stream) {
    Bitmap result = BitmapFactory.decodeStream(stream);
    return new BaseMMBean(result);
  }

  public BaseMMBean(Bitmap bitmap) {
    this.dataType = TYPE_BITMAP;
    this.dataBitmap = bitmap;
  }

  public BaseMMBean(InputStream stream) {
    try {
      byte[] typeBytes = new byte[LENGTH_TYPE_BYTE];
      stream.read(typeBytes);
      ByteBuffer byteBuffer = ByteBuffer.wrap(typeBytes);
      dataType = byteBuffer.getInt();
      byte[] sizeBytes = new byte[LENGTH_SIZE_BYTE];
      stream.read(sizeBytes);
      byteBuffer = ByteBuffer.wrap(sizeBytes);
      dataSize = byteBuffer.getLong();

      constructData(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void toStream(OutputStream stream) {
    try {
      byte[] typeBytes = ByteBuffer.allocate(LENGTH_TYPE_BYTE).putInt(dataType).array();
      byte[] sizeBytes = ByteBuffer.allocate(LENGTH_SIZE_BYTE).putLong(dataSize).array();
      stream.write(typeBytes);
      stream.write(sizeBytes);
      writeToData(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void constructData(InputStream stream) {
    try {
      switch (getDataType()) {
        case TYPE_BYTE:
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] buffer = new byte[BUFFER_SIZE];
          int len;
          while ((len = stream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
          }
          baos.flush();
          this.data = baos.toByteArray();
        case TYPE_BITMAP:
          this.dataBitmap = BitmapFactory.decodeStream(stream);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeToData(OutputStream stream) {
    try {
      switch (getDataType()) {
        case TYPE_BYTE:
          stream.write(data);
          break;
        case TYPE_BITMAP:
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          dataBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
          stream.write(byteArrayOutputStream.toByteArray());
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

  public int getDataType() {
    return dataType;
  }

  public long getDataSize() {
    return dataSize;
  }
}

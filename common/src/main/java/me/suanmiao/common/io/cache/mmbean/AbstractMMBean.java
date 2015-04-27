package me.suanmiao.common.io.cache.mmbean;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by suanmiao on 15/4/22.
 */
public abstract class AbstractMMBean implements ISizeable{
  public static final int TYPE_NONE = -1;
  public static final int TYPE_BITMAP = 1;
  public static final int TYPE_BIG_BITMAP = 2;
  public static final int TYPE_BYTE = 3;

  public static final int LENGTH_TYPE_BYTE = 4;
  public static final int LENGTH_SIZE_BYTE = 16;

  protected int dataType;
  protected long dataSize;

  public AbstractMMBean() {}

  public void toStream(OutputStream stream) {
    try {
      byte[] typeBytes = ByteBuffer.allocate(LENGTH_TYPE_BYTE).putInt(dataType).array();
      byte[] sizeBytes = ByteBuffer.allocate(LENGTH_SIZE_BYTE).putLong(dataSize).array();
      stream.write(typeBytes);
      stream.write(sizeBytes);
      writeData(stream);
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public abstract void writeData(OutputStream stream);

  public int getDataType() {
    return dataType;
  }

  public long getDataSize() {
    return dataSize;
  }
}

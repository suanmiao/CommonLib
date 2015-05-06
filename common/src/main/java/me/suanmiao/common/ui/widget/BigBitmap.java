package me.suanmiao.common.ui.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by suanmiao on 15/4/7.
 */
public class BigBitmap {
  private Bitmap[][] matrix;
  private int rowCount = 0;
  private int columnCount = 0;
  private int totalSize;

  public static final int MAX_ITEM_WIDTH = 900;
  public static final int MAX_ITEM_HEIGHT = 900;
  private int totalWidth, totalHeight;

  public BigBitmap(int rowCount, int columnCount, Bitmap[][] matrix) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.matrix = matrix;
    this.totalSize = 0;
    totalWidth = 0;
    totalHeight = 0;
    if (this.matrix != null) {
      for (int x = 0; x < columnCount; x++) {
        for (int y = 0; y < rowCount; y++) {
          totalSize += matrix[x][y].getByteCount();
          if (y == 0) {
            totalWidth += matrix[x][y].getWidth();
          }
          if (x == 0) {
            totalHeight += matrix[x][y].getHeight();
          }
        }
      }
    }
  }

  public BigBitmap(byte[] data) {
    init(data);
  }

  private void init(byte[] data) {
    this.totalSize = data.length;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(data, 0, data.length, options);
    int sourceWidth = options.outWidth;
    int sourceHeight = options.outHeight;
    totalHeight = sourceHeight;
    totalWidth = sourceWidth;
    int matrixColumn = Math.max((int) Math.ceil((float) sourceWidth / MAX_ITEM_WIDTH), 1);
    int matrixRow = Math.max((int) Math.ceil((float) sourceHeight / MAX_ITEM_HEIGHT), 1);
    rowCount = matrixRow;
    columnCount = matrixColumn;
    matrix = new Bitmap[matrixColumn][matrixRow];
    totalSize = 0;
    for (int x = 0; x < matrixColumn; x++) {
      for (int y = 0; y < matrixRow; y++) {
        int itemWidth, itemHeight;
        if (sourceWidth - x * MAX_ITEM_WIDTH <= MAX_ITEM_WIDTH) {
          itemWidth = sourceWidth - x * MAX_ITEM_WIDTH;
        } else {
          itemWidth = MAX_ITEM_WIDTH;
        }
        if (sourceHeight - y * MAX_ITEM_HEIGHT <= MAX_ITEM_HEIGHT) {
          itemHeight = sourceHeight - y * MAX_ITEM_HEIGHT;
        } else {
          itemHeight = MAX_ITEM_HEIGHT;
        }
        try {
          BitmapRegionDecoder regionDecoder =
              BitmapRegionDecoder.newInstance(data, 0, data.length, true);
          Rect itemRect =
              new Rect(x * MAX_ITEM_WIDTH, y * MAX_ITEM_HEIGHT, x * MAX_ITEM_WIDTH + itemWidth, y
                  * MAX_ITEM_HEIGHT + itemHeight);
          matrix[x][y] = regionDecoder.decodeRegion(itemRect, new BitmapFactory.Options());
          totalSize += matrix[x][y].getByteCount();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    data = null;
  }

  /**
   * data structure:
   * |matrixWidth|matrixHeight|item1Size|item2Size|data1|data2|...|
   */
  private static final int LENGTH_INT_TO_BYTE = 4;

  public void toStream(OutputStream stream) {
    try {
      stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(columnCount).array());
      stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(rowCount).array());
      for (int x = 0; x < columnCount; x++) {
        for (int y = 0; y < rowCount; y++) {
          ByteArrayOutputStream itemOutputStream = new ByteArrayOutputStream();
          matrix[x][y].compress(Bitmap.CompressFormat.JPEG, 100, itemOutputStream);
          byte[] itemData = itemOutputStream.toByteArray();
          itemOutputStream.close();
          stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(itemData.length).array());
          stream.write(itemData);
          itemData = null;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static BigBitmap fromStream(InputStream stream) {
    try {
      int columnCount = readInt(LENGTH_INT_TO_BYTE, stream);
      int rowCount = readInt(LENGTH_INT_TO_BYTE, stream);
      Bitmap[][] matrix = new Bitmap[columnCount][rowCount];
      for (int x = 0; x < columnCount; x++) {
        for (int y = 0; y < rowCount; y++) {
          int size = readInt(LENGTH_INT_TO_BYTE, stream);
          byte[] itemData = new byte[size];
          stream.read(itemData);
          matrix[x][y] = BitmapFactory.decodeByteArray(itemData, 0, itemData.length);
          itemData = null;
        }
      }
      return new BigBitmap(rowCount, columnCount, matrix);

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

  private static int readInt(int length, InputStream stream) {
    try {
      byte[] data = new byte[length];
      stream.read(data);
      ByteBuffer byteBuffer = ByteBuffer.wrap(data);
      return byteBuffer.getInt();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int getTotalHeight() {
    return totalHeight;
  }

  public int getTotalWidth() {
    return totalWidth;
  }

  public Bitmap[][] getMatrix() {
    return matrix;
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getRowCount() {
    return rowCount;
  }

  public int getTotalSize() {
    return totalSize;
  }
}

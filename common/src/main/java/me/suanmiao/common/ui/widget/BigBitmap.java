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

  public static final int MAX_ITEM_WIDTH = 500;
  public static final int MAX_ITEM_HEIGHT = 500;
  private int totalWidth, totalHeight;

  public BigBitmap(int rowCount, int columnCount, Bitmap[][] matrix) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.matrix = matrix;
  }

  public BigBitmap(byte[] data) {
    init(data);
  }

  private void init(byte[] data) {
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
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * data structure:
   * |matrixWidth|matrixHeight|item1Size|item2Size|data1|data2|...|
   */
  private static final int LENGTH_INT_TO_BYTE = 2;

  public void toStream(OutputStream stream) {
    try {
      stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(columnCount).array());
      stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(rowCount).array());
      for (int x = 0; x < columnCount; x++) {
        for (int y = 0; y < rowCount; y++) {
          ByteArrayOutputStream itemOutputStream = new ByteArrayOutputStream();
          matrix[x][y].compress(Bitmap.CompressFormat.JPEG, 100, itemOutputStream);
          byte[] itemData = itemOutputStream.toByteArray();

          stream.write(ByteBuffer.allocate(LENGTH_INT_TO_BYTE).putInt(itemData.length).array());
          stream.write(itemData);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static BigBitmap fromStream(InputStream stream) {
    int columnCount = readInt(LENGTH_INT_TO_BYTE, stream);
    int rowCount = readInt(LENGTH_INT_TO_BYTE, stream);
    Bitmap[][] matrix = new Bitmap[columnCount][rowCount];
    for (int x = 0; x < columnCount; x++) {
      for (int y = 0; y < rowCount; y++) {
        int size = readInt(LENGTH_INT_TO_BYTE, stream);
        try {
          byte[] itemData = new byte[size];
          stream.read(itemData);
          matrix[x][y] = BitmapFactory.decodeByteArray(itemData, 0, itemData.length);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return new BigBitmap(rowCount, columnCount, matrix);
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
}

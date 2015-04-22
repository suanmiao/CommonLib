package me.suanmiao.common.ui.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by suanmiao on 15/4/7.
 */
public class BigDrawable extends Drawable {
  BigBitmap mBigMap;
  private Paint mPaint;

  public BigDrawable(BigBitmap bigBitmap) {
    mBigMap = bigBitmap;
    mPaint = new Paint();
  }

  public BigDrawable(byte[] data) {
    mBigMap = new BigBitmap(data);
    mPaint = new Paint();
  }

  @Override
  public void draw(Canvas canvas) {
    if (mBigMap != null && mBigMap.getMatrix() != null) {
      for (int x = 0; x < mBigMap.getColumnCount(); x++) {
        for (int y = 0; y < mBigMap.getRowCount(); y++) {
          int left = x * BigBitmap.MAX_ITEM_WIDTH;
          int top = y * BigBitmap.MAX_ITEM_HEIGHT;
          if (mBigMap.getMatrix()[x][y] != null) {
            int itemWidth = mBigMap.getMatrix()[x][y].getWidth();
            int itemHeight = mBigMap.getMatrix()[x][y].getHeight();
            Rect itemRect = new Rect(left, top, left + itemWidth, top + itemHeight);
            Rect dstRect = new Rect(itemRect);
            mPaint.setColor(Color.RED);
            canvas.drawBitmap(mBigMap.getMatrix()[x][y], left, top, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2);
            // canvas.drawRect(itemRect, mPaint);
            // canvas.drawBitmap(mBigMap.matrix[x][y], dstRect, itemRect, mPaint);
          }
        }
      }
    }
  }

  @Override
  public void setAlpha(int alpha) {
    mPaint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(ColorFilter cf) {

  }

  @Override
  public int getOpacity() {
    return 0;
  }

  @Override
  public int getIntrinsicHeight() {
    if (mBigMap != null) {
      return mBigMap.getTotalHeight();
    }
    return 0;
  }

  @Override
  public int getIntrinsicWidth() {
    if (mBigMap != null) {
      return mBigMap.getTotalWidth();
    }
    return 0;
  }

  public void recycle() {
    if (mBigMap != null) {
      for (int x = 0; x < mBigMap.getColumnCount(); x++) {
        for (int y = 0; y < mBigMap.getRowCount(); y++) {
          mBigMap.getMatrix()[x][y].recycle();
        }
      }
      mBigMap = null;
    }
  }
}

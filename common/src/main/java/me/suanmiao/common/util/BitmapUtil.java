package me.suanmiao.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.util.helper.SystemHelper;


/**
 * Created by lhk on 3/14/14.
 */
public class BitmapUtil {

  private static final int BUFFER_SIZE = 512;

  private static final float READING_TAKE_UP_PERCENT = 0.9f;

  private BitmapUtil() {}

  public static Bitmap decodePhoto(byte[] data, Photo photo) throws IOException {
    if (photo.getContentLength() > 0) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeByteArray(data, 0, data.length, options);
      int sourceWidth = options.outWidth;
      int sourceHeight = options.outHeight;
      int sampleSize =
          getBestFitSampleSize(sourceWidth, sourceHeight, photo.getViewWidth(),
              photo.getViewHeight(), Bitmap.Config.ARGB_8888);
      options.inJustDecodeBounds = false;
      options.inSampleSize = sampleSize;
      Bitmap result = BitmapFactory.decodeByteArray(data, 0, data.length, options);
      if (photo.getProgressListener() != null && photo.getContentLength() > 0) {
        photo.getProgressListener().onProgress(1.0f);
      }
      return result;
    } else {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeByteArray(data, 0, data.length, options);
      int sourceWidth = options.outWidth;
      int sourceHeight = options.outHeight;
      int sampleSize =
          getBestFitSampleSize(sourceWidth, sourceHeight, photo.getViewWidth(),
              photo.getViewHeight(), Bitmap.Config.ARGB_8888);
      options.inJustDecodeBounds = false;
      options.inSampleSize = sampleSize;

      Bitmap result = BitmapFactory.decodeByteArray(data, 0, data.length, options);
      if (photo.getProgressListener() != null && photo.getContentLength() > 0) {
        photo.getProgressListener().onProgress(1.0f);
      }
      return result;
    }
  }

  public static Bitmap decodePhoto(InputStream in, Photo photo) throws IOException {
    if (photo.getContentLength() > 0) {
      BufferedInputStream inputStream = new BufferedInputStream(in);
      inputStream.mark(photo.getContentLength());
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(inputStream, null, options);
      int sourceWidth = options.outWidth;
      int sourceHeight = options.outHeight;
      int sampleSize =
          getBestFitSampleSize(sourceWidth, sourceHeight, photo.getViewWidth(),
              photo.getViewHeight(), Bitmap.Config.ARGB_8888);
      options.inJustDecodeBounds = false;
      options.inSampleSize = sampleSize;
      inputStream.reset();

      Bitmap result = BitmapFactory.decodeStream(inputStream, null, options);
      if (photo.getProgressListener() != null && photo.getContentLength() > 0) {
        photo.getProgressListener().onProgress(1.0f);
      }
      return result;
    } else {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int len;
      while ((len = in.read(buffer)) > -1) {
        baos.write(buffer, 0, len);
      }
      baos.flush();

      InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
      InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(is1, null, options);
      int sourceWidth = options.outWidth;
      int sourceHeight = options.outHeight;
      int sampleSize =
          getBestFitSampleSize(sourceWidth, sourceHeight, photo.getViewWidth(),
              photo.getViewHeight(), Bitmap.Config.ARGB_8888);
      options.inJustDecodeBounds = false;
      options.inSampleSize = sampleSize;

      Bitmap result = BitmapFactory.decodeStream(is2, null, options);
      if (photo.getProgressListener() != null && photo.getContentLength() > 0) {
        photo.getProgressListener().onProgress(1.0f);
      }
      return result;
    }
  }

  public static byte[] streamToByteArray(InputStream in, Photo photo) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    byte[] buffer = new byte[BUFFER_SIZE];
    int nRead;
    float readSize = 0;

    while ((nRead = in.read(buffer, 0, buffer.length)) != -1) {
      outputStream.write(buffer, 0, nRead);
      readSize += nRead;
      if (photo.getProgressListener() != null && photo.getContentLength() > 0) {
        photo.getProgressListener().onProgress(
            (readSize / photo.getContentLength() * READING_TAKE_UP_PERCENT));
      }
    }
    outputStream.flush();
    return outputStream.toByteArray();
  }


  public static int getBestFitSampleSize(int sourceWidth, int sourceHeight, int targetWidth,
      int targetHeight, Bitmap.Config config) {
    /**
     * step:
     * 1.check if the target size OOOM >> decode into biggest size
     * 2.check if the target size bigger than source size >> decode into source size
     *
     * size of one pixel in different config:
     * ARGB_8888 : 4
     * ARGB_4444 : 2
     * RGB_565 : 2
     * ALPHA_8 : 1
     */
    int sizePerPixel = 4;
    switch (config) {
      case ARGB_8888:
        sizePerPixel = 4;
        break;
      case ARGB_4444:
        sizePerPixel = 2;
        break;
      case RGB_565:
        sizePerPixel = 2;
        break;
      case ALPHA_8:
        sizePerPixel = 1;
        break;
    }
    int maxMemory = (int) (Runtime.getRuntime().maxMemory());
    int maxBitmapSize = maxMemory / sizePerPixel;
    if (targetHeight * targetWidth > maxBitmapSize) {
      double ratio = (double) (targetHeight * targetWidth) / (double) (maxBitmapSize);
      targetHeight = (int) (targetHeight / ratio);
      targetWidth = (int) (targetWidth / ratio);
    }

    double widthRatio = (double) (sourceWidth) / (double) (targetWidth);
    double heightRatio = (double) (sourceHeight) / (double) (targetHeight);

    return (int) Math.round(Math.min(widthRatio, heightRatio));
  }

  public static Bitmap decodeInputStream(InputStream in, boolean sample) throws IOException {
    if (sample) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int len;
      while ((len = in.read(buffer)) > -1) {
        baos.write(buffer, 0, len);
      }
      baos.flush();

      InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
      InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(is1, null, options);
      int sourceWidth = options.outWidth;
      int sourceHeight = options.outHeight;
      int[] screenSize = SystemHelper.getScreenSize();
      int sampleSize =
          getBestFitSampleSize(sourceWidth, sourceHeight, screenSize[0], screenSize[1],
              Bitmap.Config.ARGB_8888);
      options.inJustDecodeBounds = false;
      options.inSampleSize = sampleSize;
      return BitmapFactory.decodeStream(is2, null, options);
    } else {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = false;
      return BitmapFactory.decodeStream(in, null, options);
    }
  }

  public static Bitmap decodeFile(String path) {
    Bitmap bitmap = null;

    int maxMemory = (int) (Runtime.getRuntime().maxMemory());
    try {
      FileInputStream fileInputStream = new FileInputStream(path);
      byte[] content = readAllBytes(fileInputStream);

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;

      BitmapFactory.decodeByteArray(content, 0, content.length, options);

      int width = options.outWidth;
      int height = options.outHeight;
      if (width * height > maxMemory / 2) {
        options.inSampleSize = (int) Math.sqrt((height * width) / maxMemory);
      }
      options.inJustDecodeBounds = false;
      bitmap = BitmapFactory.decodeByteArray(content, 0, content.length, options);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return bitmap;
  }

  public static byte[] readAllBytes(InputStream inputStream) {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int nRead;
      byte[] data = new byte[10000];

      while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }

      buffer.flush();

      return buffer.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}

package me.suanmiao.common.io.cache;

import android.app.ActivityManager;
import android.content.Context;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.suanmiao.common.io.cache.generator.CommonMMBeanGenerator;
import me.suanmiao.common.io.cache.generator.IMMBeanGenerator;
import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;

/**
 * Created by suanmiao on 14-11-29.
 */
public class CacheManager {
  private LruBeanCache<String, AbstractMMBean> ramCache;
  private DiskMMCache diskBitmapCache;
  private static final int APP_VERSION = 1;

  private static final int MB = 1024 * 1024;
  private static final float BITMAP_MEMORY_CACHE_SIZE_SCALE = 0.15f; // 15% memory
  private static final int BITMAP_MAX_FILE_CACHE_SIZE = 16 * MB; // 16M
  private static final int MAX_RAM_CACHE_ITEM_SIZE = 1 * MB;

  private IMMBeanGenerator immBeanGenerator;

  public CacheManager(String diskBitmapPath, Context context) {
    ramCache = new LruBeanCache<>(getMemoryCacheSize(context));
    try {
      diskBitmapCache = new DiskMMCache(diskBitmapPath, APP_VERSION, BITMAP_MAX_FILE_CACHE_SIZE);
      setBeanGenerator(new CommonMMBeanGenerator());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setBeanGenerator(IMMBeanGenerator generator) {
    immBeanGenerator = generator;
    diskBitmapCache.setBeanGenerator(generator);
  }

  public IMMBeanGenerator getBeanGenerator() {
    return immBeanGenerator;
  }

  public AbstractMMBean get(String originalKey) throws IOException {
    if (ramCache == null || originalKey == null) {
      return null;
    }
    AbstractMMBean ramResult = getFromRam(originalKey);
    if (ramResult != null) {
      return ramResult;
    }
    AbstractMMBean diskResult = getFromDisk(originalKey);
    if (diskResult != null) {
      putToRam(originalKey, diskResult);
      return diskResult;
    }
    return null;
  }

  public AbstractMMBean getFromRam(String originalKey) throws IOException {
    if (ramCache == null || originalKey == null) {
      return null;
    }
    originalKey = getHashKey(originalKey);
    return ramCache.get(originalKey);
  }

  public AbstractMMBean getFromDisk(String originalKey) throws IOException {
    if (diskBitmapCache == null || originalKey == null) {
      return null;
    }
    originalKey = getHashKey(originalKey);
    return diskBitmapCache.get(originalKey);
  }

  public boolean put(String originKey, AbstractMMBean value, boolean cacheToDisk)
      throws IOException {
    if (value.getSize() < MAX_RAM_CACHE_ITEM_SIZE) {
      putToRam(originKey, value);
      if (cacheToDisk) {
        return putToDisk(originKey, value);
      }
    } else {
      putToDisk(originKey, value);
    }
    return false;
  }

  public boolean putToRam(String originKey, AbstractMMBean value) {
    if (originKey != null && value != null) {
      originKey = getHashKey(originKey);
      ramCache.put(originKey, value);
      return true;
    } else {
      return false;
    }
  }

  public boolean putToDisk(String originKey, AbstractMMBean value) {
    if (originKey != null && value != null && diskBitmapCache != null) {
      originKey = getHashKey(originKey);
      try {
        diskBitmapCache.put(originKey, value);
        return true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public String getHashKey(String key) {
    String cacheKey;
    try {
      final MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(key.getBytes());
      cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
  }

  private String bytesToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xFF & bytes[i]);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  private int getMemoryCacheSize(Context context) {
    int memoryClass =
        ((ActivityManager) context.getSystemService(
            Context.ACTIVITY_SERVICE))
            .getMemoryClass();
    return Math.round(memoryClass * MB * BITMAP_MEMORY_CACHE_SIZE_SCALE);
  }

}

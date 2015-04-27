package me.suanmiao.common.io.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.suanmiao.common.io.cache.generator.IMMBeanGenerator;
import me.suanmiao.common.io.cache.mmbean.AbstractMMBean;

/**
 * Created by suanmiao on 14-11-29.
 */
public class DiskMMCache {
  private DiskLruCache diskCache;
  private static final int BITMAP_VALUE_INDEX = 0;
  private IMMBeanGenerator immBeanGenerator;

  public DiskMMCache(String diskPath, int APP_VERSION,
      long diskSize) throws IOException {
    diskCache = DiskLruCache.open(new File(diskPath), APP_VERSION, 1, diskSize);
  }

  public void setBeanGenerator(IMMBeanGenerator immBeanGenerator) {
    this.immBeanGenerator = immBeanGenerator;
  }

  public AbstractMMBean get(String key) throws IOException {
    if (key == null) {
      return null;
    }
    DiskLruCache.Snapshot snapshot = diskCache.get(key);
    if (snapshot != null) {
      InputStream in = diskCache.get(key).getInputStream(BITMAP_VALUE_INDEX);
      return immBeanGenerator.generateMMBeanFromTotalStream(in);
    }
    return null;
  }

  public boolean put(String key, AbstractMMBean value) throws IOException {
    if (key != null || value != null) {
      DiskLruCache.Editor editor = diskCache.edit(key);
      if (editor != null) {
        OutputStream outputStream = editor.newOutputStream(BITMAP_VALUE_INDEX);
        value.toStream(outputStream);
        editor.commit();
        return true;
      }
    }
    return false;
  }

}

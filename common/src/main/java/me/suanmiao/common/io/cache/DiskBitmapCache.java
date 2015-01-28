package me.suanmiao.common.io.cache;

import android.graphics.Bitmap;

import me.suanmiao.common.util.BitmapUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by suanmiao on 14-11-29.
 */
public class DiskBitmapCache {
    private DiskLruCache diskCache;
    private static final int BITMAP_VALUE_INDEX = 0;

    public DiskBitmapCache(String diskPath, int APP_VERSION, long diskSize) throws IOException {
        diskCache = DiskLruCache.open(new File(diskPath), APP_VERSION, 1, diskSize);
    }

    public DiskBitmapCache(DiskLruCache diskCache) throws IOException {
        this.diskCache = diskCache;
    }

    public Bitmap get(String key) throws IOException {
        if (key == null) {
            return null;
        }
        DiskLruCache.Snapshot snapshot = diskCache.get(key);
        if (snapshot != null) {
            InputStream in = diskCache.get(key).getInputStream(BITMAP_VALUE_INDEX);
            return BitmapUtil.decodeInputStream(in, false);
        }
        return null;
    }

    public boolean put(String key, Bitmap value) throws IOException {
        if (key != null || value != null) {
            DiskLruCache.Editor editor = diskCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(BITMAP_VALUE_INDEX);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.PNG, 100, baos);
            outputStream.write(baos.toByteArray());
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

}

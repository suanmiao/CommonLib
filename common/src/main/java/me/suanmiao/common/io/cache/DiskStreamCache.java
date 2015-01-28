package me.suanmiao.common.io.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by suanmiao on 14-11-29.
 */
public class DiskStreamCache {
    private DiskLruCache diskCache;
    private static final int STREAM_VALUE_INDEX = 1;
    private static final int BUFFER_SIZE = 512;

    public DiskStreamCache(String diskPath, int APP_VERSION, long diskSize) throws IOException {
        diskCache = DiskLruCache.open(new File(diskPath), APP_VERSION, 1, diskSize);
    }

    public DiskStreamCache(DiskLruCache diskCache) throws IOException {
        this.diskCache = diskCache;
    }

    public InputStream get(String key) throws IOException {
        if (key == null) {
            return null;
        }
        DiskLruCache.Snapshot snapshot = diskCache.get(key);
        if (snapshot != null) {
            return diskCache.get(key).getInputStream(STREAM_VALUE_INDEX);
        }
        return null;
    }

    public boolean put(String key, InputStream in) throws IOException {
        if (key != null || in != null) {
            DiskLruCache.Editor editor = diskCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(STREAM_VALUE_INDEX);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}

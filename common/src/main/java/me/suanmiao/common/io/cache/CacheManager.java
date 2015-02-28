package me.suanmiao.common.io.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by suanmiao on 14-11-29.
 */
public class CacheManager {
    private LruCache<String, Bitmap> ramCache;
    private DiskBitmapCache diskBitmapCache;
    private static final int APP_VERSION = 1;

    private static final int MB = 1024 * 1024;
    private static final float BITMAP_MEMORY_CACHE_SIZE_SCALE = 0.15f; // 15% memory
    private static final int BITMAP_MAX_FILE_CACHE_SIZE = 16 * MB; // 16M

    public CacheManager(String diskBitmapPath, Context context) {
        ramCache = new LruCache<>(getMemoryCacheSize(context));
        try {
            DiskLruCache diskBitmapLruCache = DiskLruCache.open(new File(diskBitmapPath), APP_VERSION, 1, BITMAP_MAX_FILE_CACHE_SIZE);
            diskBitmapCache = new DiskBitmapCache(diskBitmapLruCache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap get(String originalKey) throws IOException {
        if (ramCache == null || originalKey == null) {
            return null;
        }
        Bitmap ramResult = getFromRam(originalKey);
        if (ramResult != null) {
            return ramResult;
        }
        Bitmap diskResult = getFromDisk(originalKey);
        if (diskResult != null) {
            putToRam(originalKey, diskResult);
            return diskResult;
        }
        return null;
    }

    public Bitmap getFromRam(String originalKey) throws IOException {
        if (ramCache == null || originalKey == null) {
            return null;
        }
        originalKey = getHashKey(originalKey);
        return ramCache.get(originalKey);
    }

    public Bitmap getFromDisk(String originalKey) throws IOException {
        if (diskBitmapCache == null || originalKey == null) {
            return null;
        }
        originalKey = getHashKey(originalKey);
        return diskBitmapCache.get(originalKey);
    }

    public boolean put(String originKey, Bitmap value, boolean cacheToDisk) throws IOException {
        putToRam(originKey, value);
        if(cacheToDisk){
            return putToDisk(originKey,value);
        }
        return false;
   }

    public boolean putToRam(String originKey, Bitmap value) {
        if (originKey != null && value != null) {
            originKey = getHashKey(originKey);
            ramCache.put(originKey, value);
            return true;
        } else {
            return false;
        }
    }

    public boolean putToDisk(String originKey, Bitmap value) {
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

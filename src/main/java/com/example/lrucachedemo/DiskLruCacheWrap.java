package com.example.lrucachedemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haif on 2019/2/17.
 */

public class DiskLruCacheWrap {

    private Context context;
    private DiskLruCache diskLruCache;

    public DiskLruCacheWrap(Context context) {
        this.context = context;
        initDiskLruCache();
    }

    /**
     * 创建DiskLruCache实例
     */
    private void initDiskLruCache() {
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入缓存
     * 创建DiskLruCache.Editor实例，调用它的newOutputStream()方法来创建一个输出流，实现下载并写入缓存的功能
     */
    public void putBitmapToDiskCache(final String imageUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = hashKeyForDisk(imageUrl);

                    // 创建DiskLruCache.Editor实例，接收一个参数key，即缓存文件的文件名（文件名要与URL一一对应，对URL进行MD5加密得到）
                    DiskLruCache.Editor editor = diskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream os = editor.newOutputStream(0);

                        // 在写入操作执行完之后，需要commit()进行提交；或者abort()方法放弃此次写入
                        if (downloadUrlToStream(imageUrl, os)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }

                    //
                    diskLruCache.flush();

                } catch (IOException e) {

                }

            }
        }).start();
    }

    /**
     * 读取缓存
     * 调用DiskLruCache的get()方法，获取到DiskLruCache.Snapshot对象，再调用getInputStream()获取缓存文件的输入流
     */
    public Bitmap getBitmapFromDiskCache(String imageUrl) {
        try {
            String key = hashKeyForDisk(imageUrl);
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);

            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除缓存
     */
    public void removeBitmapFromDiskCache(String imageUrl) {
        try {
            String key = hashKeyForDisk(imageUrl);
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取硬盘缓存的文件路径
     * uniqueName 为了对不同类型的数据进行区分而设定的一个唯一值，比如bitmap、object等文件夹
     */

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) { // 当SD卡存在或者SD卡不可被移除时
            cachePath = context.getExternalCacheDir().getPath();    // 即sdcard/Android/data/<application package>/cache
        } else {
            cachePath = context.getCacheDir().getPath();    // 即data/data/<application package>/cache
        }
        return new File(cachePath + File.separator + uniqueName);   // File.separator，系统目录的分隔符，就是斜线。Windows和Linux下的路径分隔符是不一样的，如果使用绝对路径，跨平台会报错
    }

    /**
     * 获取版本号
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 下载图片到本地
     */
    public boolean downloadUrlToStream(String urlString, OutputStream os) {
        // 将图片下载下来
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            bis = new BufferedInputStream(conn.getInputStream(), 8 * 1024);
            bos = new BufferedOutputStream(os, 8 * 1024);

            int temp;
            while ((temp = bis.read()) != -1) {
                bos.write(temp);
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 缓存文件的文件名
     * 这个文件名要和图片的URL一一对应，直接使用URL作为key？不合适，因为图片URL可能包含一些特殊字符，这些字符有可能在命名文件时是不合法的
     * 最简单的做法是将图片的URL进行MD5编码，编码后的字符串肯定是唯一的，并且只会包含0-F这样的字符
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            cacheKey = bytes2HexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节数组转成十六进制字符串
     */
    private String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}

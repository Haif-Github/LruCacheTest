package com.example.lrucachedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gengtiantao on 2019/2/12.
 */

public class LruCacheWrap {

    private final LruCache<String, Bitmap> lruCache;

    public LruCacheWrap() {
        // 获取最大可用的内存空间，使用内存超出这个值会引起OutOfMemory异常（LruCache通过构造函数传入缓存值，以KB为单位）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用系统分配给应用程序的八分之一内存来作为缓存大小
        int cacheSize = maxMemory / 8;

        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            // 计算出要缓存的每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    // 从缓存中读取图片
    public Bitmap getBitmapFromCache(String url) {
        return lruCache.get(url);
    }

    // 将图片缓存起来
    public void putBitmapToCache(Bitmap bitmap, String url) {
        if (bitmap != null) {
            lruCache.put(url, bitmap);
        }
    }

    /**
     * 当ImageView加载图片
     * 首先从LruCache缓存中检查，如果图片存在，就直接更新ImageView，否则开启一个线程来加载这张图片
     */
    public void loadBitmaps(String imageUrl, ImageView iv) {
        Bitmap bitmap = getBitmapFromCache(imageUrl);
        if (bitmap != null) {    // 缓存中存在该bitmap，则使用
            iv.setImageBitmap(bitmap);
        } else {     // 缓存中不存在该bitmap，则开启一个线程来加载这张图片
            iv.setImageResource(R.mipmap.ic_launcher);
            BitmapWorkTask task = new BitmapWorkTask(iv);
            task.execute(imageUrl);
        }
    }

    // 缓存中不存在图片时，新建任务加载图片
    class BitmapWorkTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView iv;

        public BitmapWorkTask(ImageView iv) {
            this.iv = iv;
        }

        // 下载图片
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = downloadBitmap(strings[0]);
            // 下载完图片后缓存到LruCache中
            putBitmapToCache(bitmap, strings[0]);
            return bitmap;
        }

        // 显示下载好的图片
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (iv != null && bitmap != null) {
                iv.setImageBitmap(bitmap);
            }
        }

        // Http请求，获取Bitmap对象
        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(imageUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return bitmap;
        }
    }

}

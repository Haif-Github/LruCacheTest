package com.example.lrucachedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by haif on 2019/2/17.
 */

public class DiskLruCacheActivity extends AppCompatActivity {


    private ImageView imageView;
    private DiskLruCacheWrap diskLruCache;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disklrucache);

        imageView = findViewById(R.id.imageView);
        diskLruCache = new DiskLruCacheWrap(this);

        imageUrl = "https://img-my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_put:
                diskLruCache.putBitmapToDiskCache(imageUrl);
                break;
            case R.id.tv_get:
                Bitmap bitmap = diskLruCache.getBitmapFromDiskCache(imageUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                break;
            case R.id.tv_remove:
                diskLruCache.removeBitmapFromDiskCache(imageUrl);
                break;
        }
    }

}

package com.example.lrucachedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        PhotoAdapter adapter = new PhotoAdapter(this, getData());
        recyclerView.setAdapter(adapter);

    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("http://seopic.699pic.com/photo/50112/4425.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/3060.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1886.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1887.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1978.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1976.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1969.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1917.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1604.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1958.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1831.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1780.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1893.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1517.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1982.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1942.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1951.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/4050.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/8136.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/3046.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50112/4425.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/3060.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1886.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1887.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1978.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1976.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1969.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1917.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1604.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1958.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1831.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1780.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1893.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1517.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1982.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1942.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50114/1951.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/4050.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/8136.jpg_wh1200.jpg");
        list.add("http://seopic.699pic.com/photo/50113/3046.jpg_wh1200.jpg");
        return list;
    }

}

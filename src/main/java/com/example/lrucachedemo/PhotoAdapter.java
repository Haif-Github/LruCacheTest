package com.example.lrucachedemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengtiantao on 2019/2/12.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ItemViewHolder> {

    private Context context;
    private List<String> list = new ArrayList<>();
    private final BitmapCache bitmapCache;

    public PhotoAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        bitmapCache = new BitmapCache();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        bitmapCache.loadBitmaps(list.get(position), holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_iv);
        }
    }

}

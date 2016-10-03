package com.example2.diablove.login_register;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Diablove on 1/5/2016.
 */
public class ItemAdapter extends BaseAdapter {

    Context CTX;
    ArrayList<Item> items;
    ArrayList<Bitmap> bitmaps;

    public ItemAdapter(Context CTX, ArrayList<Item> items , ArrayList<Bitmap> bitmaps) {

        this.CTX = CTX;
        this.items = items;
        this.bitmaps = bitmaps;
    }

    public ItemAdapter(){

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        if(convertView == null)
        {
            img = new ImageView(CTX);
            img.setLayoutParams(new GridView.LayoutParams(160,160));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setPadding(8,8,8,8);
        }
        else
        {
            img = (ImageView) convertView;
        }

        img.setImageBitmap(bitmaps.get(position));
        return img;
    }
}

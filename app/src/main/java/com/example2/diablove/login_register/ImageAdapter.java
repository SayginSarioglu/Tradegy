package com.example2.diablove.login_register;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Diablove on 1/1/2016.
 */
public class ImageAdapter extends BaseAdapter {


    private Context CTX;
    private Integer image_id[] = {R.drawable.other, R.drawable.fashion, R.drawable.elecs, R.drawable.travel};
    public ImageAdapter(Context CTX)
    {
        this.CTX = CTX;
    }
    @Override
    public int getCount() {
        return image_id.length;
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

        img.setImageResource(image_id[position]);
        return img;

    }





}

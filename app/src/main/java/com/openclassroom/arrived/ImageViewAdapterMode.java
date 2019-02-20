package com.openclassroom.arrived;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageViewAdapterMode extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mInflater;
    private boolean isSelected;
    private int nbItemSelet = 0;
    String name = null;
    private Integer[] mThumbIds = {R.drawable.pieds, R.drawable.voiture, R.drawable.velo, R.drawable.metro,
            R.drawable.tram, R.drawable.bus, R.drawable.train};
    private String[] mListTag = {"pieds", "voiture", "velo", "metro", "tram", "bus", "train"};
    private static final String TAG = "ImageV";

    public ImageViewAdapterMode(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getNbItemSelet() {
        return this.nbItemSelet;
    }

    public void setNbItemSelet(int nbItemSelet) {
        this.nbItemSelet = nbItemSelet;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setAlpha(0.30f);
            imageView.setTag(mListTag[position]);
        } else {
            imageView = (ImageView) convertView;
            if (isSelected == false) {
                imageView.setAlpha(0.30f);
            } else {
                imageView.setAlpha(1.00f);
            }
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}
package com.openclassroom.arrived;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageViewAdapterMeteo extends BaseAdapter {

    private Context mContext;
    private boolean mIsSelect;
    private int mNbItemSelet = 0;
    private Integer[] mThumbIds = {R.drawable.sunny, R.drawable.cloudy, R.drawable.rain, R.drawable.snow};
    private String[] mListTag = {"sunny", "cloudy", "rain", "snow"};

    public ImageViewAdapterMeteo(Context c) {
        mContext = c;
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

    public int getmNbItemSelet() {
        return this.mNbItemSelet;
    }

    public void setmNbItemSelet(int mNbItemSelet) {
        this.mNbItemSelet = mNbItemSelet;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setAlpha(0.30f);
            imageView.setSelected(false);
            imageView.setTag(mListTag[position]);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}
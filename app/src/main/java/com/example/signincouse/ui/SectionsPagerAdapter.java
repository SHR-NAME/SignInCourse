package com.example.signincouse.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.signincouse.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    public SectionsPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                 return FragmentMain.newInstance(position+1);
            case 1:
                 return FragmentCouse.newInstance(position+1);
            case 2:
                 return FragmentNotice.newInstance(position+1);
            case 3:
                return FragmentMe.newInstance(position+1);
        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
    //没选中的tab图片
    private int[]imageId={
            R.drawable.tabhome,
            R.drawable.tabcourse,
            R.drawable.tabnotification,
            R.drawable.tabuser
    };
    //选中的tab图片
    private int tabImagepressId[]={R.drawable.tabhome_press,
                                       R.drawable.tabcourse_press,
                                       R.drawable.tabnotification_press,
                                       R.drawable.tabuser_press
    };
    public int getImageId(int i){
        return imageId[i];
    }
    public int getTabImagepressId(int i){
        return tabImagepressId[i];
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
    public View getTabView(int position){
        if (position==0){
            ImageView imageView=new ImageView(context);
            imageView.setImageResource(tabImagepressId[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        }
        ImageView imageView=new ImageView(context);
        imageView.setImageResource(imageId[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }
}

package com.lyj.bannerlib.Banner;

import android.view.View;

/**
 * Created by yu on 2017/9/14.
 */

public abstract class BannerAdapter {


    /**
     * 根据位置获取VIewPager里面的子View
     * @param position
     * @return
     */
    public abstract View getView(int position,View convertView);


    /**
     * 返回有多少张图片
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 根据位置获取图片的描述
     * @return
     */
    public String getDesc(int position){
        return "";
    }

}

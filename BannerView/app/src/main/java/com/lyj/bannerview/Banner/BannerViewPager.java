package com.lyj.bannerview.Banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import java.lang.reflect.Field;

/**
 * Created by yu on 2017/9/14.
 */

public class BannerViewPager extends ViewPager {

    private final int ROLL_MSG = 0x01;
    //默认的切换时间
    private final int DEFAULT_TIME = 5000;

    private int mCountDownTime = 0;

    private BannerAdapter mBannerAdapter;
    private BannerScroller mScroller;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ROLL_MSG:
                    setCurrentItem(getCurrentItem()+1);
                    startRoll();
                    break;
            }
        }
    };



    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        //改变切换的速率
        //通过反射来改变
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            //设置为public的
            field.setAccessible(true);
            //设置参数 第一个object表示当前属性在哪个类 ,第二个参数表示field设置的值
            mScroller = new BannerScroller(context);
            field.set(this,mScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        //设置父类ViewPager的adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 开始滚动
     *
     */
    public void startRoll(){
        mHandler.removeMessages(ROLL_MSG);
        mHandler.sendEmptyMessageDelayed(ROLL_MSG,DEFAULT_TIME);
    }

    /**
     * Activity销毁的时候，会调用此方法，在此处理handler 防止内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mHandler != null){
            mHandler.removeMessages(ROLL_MSG);
            mHandler = null;
        }
    }


    /**
     * 设置滚动时候的速率
     *
     * @param scrollerDuration
     */
    public void setScrollerDuration(int scrollerDuration) {
        if(mScroller != null){
            mScroller.setScrollerDuration(scrollerDuration);
        }
    }

    /**
     * 给ViewPage设置适配器
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //为了实现无限轮播 ，设置一个最大的值
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 创建ViewPager条目的回调的方法
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //使用适配器模式，方便用户自定义
            View itemView = mBannerAdapter.getView(position);
            //这里的container就是ViewPager
            container.addView(itemView);
            return itemView;
        }

        /**
         * 销毁ViewPager条目的方法
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //释放下内存
            object = null;
        }
    }
}

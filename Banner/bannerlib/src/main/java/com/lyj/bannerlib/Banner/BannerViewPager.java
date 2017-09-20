package com.lyj.bannerlib.Banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/9/14.
 */

public class BannerViewPager extends ViewPager {

    private final int ROLL_MSG = 0x01;
    private final int DEFAULT_INTERVAL_TIME = 3000;//m默认3秒切换一次

    private BannerAdapter mBannerAdapter;
    private BannerScroller mScroller;

    private Activity mActivity;

    private int mDataSize;//多少条数据

    private int mIntervalTime = DEFAULT_INTERVAL_TIME;

    //界面复用
    private List<View> mConvertViewList;


    private boolean mAutoSwitch = true;//是不是自动切换 true自动切换 false 手动

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ROLL_MSG:
                    if(!mAutoSwitch){
                        mAutoSwitch = true;
                    }else {
                        setCurrentItem(getCurrentItem()+1);
                    }
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

        mActivity = (Activity) context;

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

        //复用界面
        mConvertViewList = new ArrayList<>();
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        mDataSize = mBannerAdapter.getCount();
        //设置父类ViewPager的adapter
        setAdapter(new BannerPagerAdapter());
        //将开始位置设置的大一些，如果设置到第一个会无法向左滑动
        setCurrentItem(100*mDataSize);
        //检测手动滑动
        setOnTouch();
        //注册生命周期回调
        mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    private void setOnTouch() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        mAutoSwitch = false;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 开始滚动
     *
     */
    public void startRoll(){
        mHandler.removeMessages(ROLL_MSG);
        mHandler.sendEmptyMessageDelayed(ROLL_MSG,mIntervalTime);
    }



    /**
     * Activity销毁的时候，会调用此方法，在此处理handler 防止内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {

        if(mHandler != null){
            mHandler.removeMessages(ROLL_MSG);
            mHandler = null;
        }
        if(mActivity != null){
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        super.onDetachedFromWindow();

    }

    /**
     * 设置多久切换一张图片
     * @param intervalTime
     */
    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
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
            View itemView = mBannerAdapter.getView(position%mDataSize,getConvertView());
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
            mConvertViewList.add((View) object);
        }

    }

    /**
     * 获取复用界面
     * @return
     */
    private View getConvertView() {
        if(mConvertViewList != null && mConvertViewList.size()>0){
            for(View view:mConvertViewList){
                if(view.getParent() == null){
                    return view;
                }
            }
        }
        return null;
    }

    Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new SimpleActivityLifeCallback(){

        @Override
        public void onActivityResumed(Activity activity) {
            //开启轮播
//            是不是监听当前Activity的生命周期
            if(activity == mActivity){
                mHandler.sendEmptyMessageDelayed(ROLL_MSG,mIntervalTime);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if(activity == mActivity){
                mHandler.removeMessages(ROLL_MSG);
            }
        }
    };
}

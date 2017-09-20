package com.lyj.bannerlib.Banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by yu on 2017/9/18.
 *
 * 用于改变切换的速率
 */


public class BannerScroller extends Scroller {

    //动画持续的时间
    private int mScrollerDuration = 1000;

    public int getScrollerDuration() {
        return mScrollerDuration;
    }


    /**
     * 设置滚动的速率
     *
     * @param scrollerDuration
     */
    public void setScrollerDuration(int scrollerDuration) {
        this.mScrollerDuration = scrollerDuration;
    }

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }
}

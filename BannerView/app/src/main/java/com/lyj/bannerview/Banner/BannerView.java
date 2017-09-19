package com.lyj.bannerview.Banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyj.bannerview.R;


/**
 * Created by yu on 2017/9/18.
 */

public class BannerView extends RelativeLayout {

    private Context mContext;

    private BannerViewPager mBannerVp;
    private TextView mTitleTv;
    private LinearLayout mDotContainerLl;

    private BannerAdapter mAdapter;

    private Drawable mFocusDotDrawable;
    private Drawable mNormalDotDrawable;

    private int mLastPosition = 0;//上次的位置

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = inflate(context, R.layout.view_banner,this);
        initView(view);
    }

    private void initView(View view) {
        mBannerVp = (BannerViewPager) view.findViewById(R.id.banner_vp);
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
        mDotContainerLl = (LinearLayout) view.findViewById(R.id.ll_dot_container);
    }

    public void setAdapter(BannerAdapter bannerAdapter){
        this.mAdapter = bannerAdapter;
        mBannerVp.setAdapter(bannerAdapter);

        mFocusDotDrawable = new ColorDrawable(Color.RED);
        mNormalDotDrawable = new ColorDrawable(Color.WHITE);
        //初始化点的指示器
        initDotIndicator();

        //初始化的时候显示第一条的描述
        mTitleTv.setText(mAdapter.getDesc(0));

        mBannerVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIndicatorStat(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 修改指示点  title
     *
     * @param position
     */
    private void changeIndicatorStat(int position) {
        position = position % mAdapter.getCount();

        //把之前选中的设置为默认
        DotIndicatorView oldIndicatorView = (DotIndicatorView) mDotContainerLl.getChildAt(mLastPosition);
        oldIndicatorView.setDrawable(mNormalDotDrawable);

        //把现在的设置为选中
        position = position % mAdapter.getCount();
        DotIndicatorView currentIndicatorView = (DotIndicatorView) mDotContainerLl.getChildAt(position);
        currentIndicatorView.setDrawable(mFocusDotDrawable);
        mLastPosition = position;

        String desc = mAdapter.getDesc(mLastPosition);
        mTitleTv.setText(desc);
    }

    /**
     * 初始化点的指示器
     */
    private void initDotIndicator() {
        int count = mAdapter.getCount();
        for(int i=0;i<count;i++){
            DotIndicatorView dotIndicatorView = new DotIndicatorView(mContext);
            //设置圆点的宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(8),dp2px(8));
            layoutParams.leftMargin = layoutParams.rightMargin = dp2px(2);

            dotIndicatorView.setLayoutParams(layoutParams);
            if(i == 0){
                dotIndicatorView.setDrawable(mFocusDotDrawable);
            }else {
                dotIndicatorView.setDrawable(mNormalDotDrawable);

            }
            mDotContainerLl.addView(dotIndicatorView);
        }
    }

    private int dp2px(int dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 开始滚动
     *
     */
    public void startRoll(){
        mBannerVp.startRoll();
    }

    public void setScrollerDuration(int scrollerDuration) {
        mBannerVp.setScrollerDuration(scrollerDuration);
    }


}

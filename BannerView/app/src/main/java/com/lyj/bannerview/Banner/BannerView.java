package com.lyj.bannerview.Banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyj.bannerview.R;


/**
 * Created by yu on 2017/9/18.
 */

public class BannerView extends RelativeLayout {

    private final int DEFAULT_COLOR = Color.TRANSPARENT;//默认底部颜色 透明
    private final int DEFAULT_INTERVAL_TIME = 3000;//m默认3秒切换一次
    private final int DEFAULT_SWITCH_TIME = 950;//默认切换时的时间

    private Context mContext;

    private BannerViewPager mBannerVp;
    private TextView mTitleTv;
    private LinearLayout mDotContainerLl;
    private RelativeLayout mBottomRl;

    private BannerAdapter mAdapter;

    private int mLastPosition = 0;//上次的位置

    //============自定义属性==============
    //点的位置
    private int mDotGravity = 0;
    //点的大小
    private int mDotSize = 2;
    //点的距离
    private int mDotDistance = 2;
    //选中点的颜色
    private Drawable mFocusDotDrawable;
    //默认点的颜色
    private Drawable mNormalDotDrawable;
    //底部的颜色
    private int mBottomColor = DEFAULT_COLOR;
    //宽高比
    private float mWidthPercent,mHeightPercent;
    //切换的间隔
    private int mIntervalTime = DEFAULT_INTERVAL_TIME;
    //切换时的时间
    private int mSwitchTime = DEFAULT_SWITCH_TIME;

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
        initAttrs(attrs);
    }

    private void initView(View view) {
        mBannerVp = (BannerViewPager) view.findViewById(R.id.banner_vp);
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
        mDotContainerLl = (LinearLayout) view.findViewById(R.id.ll_dot_container);
        mBottomRl = (RelativeLayout) view.findViewById(R.id.rl_bottom);
    }

    /**
     * 初始化自定义属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.BannerView);

        mDotGravity = typedArray.getInt(R.styleable.BannerView_dotGrivity,0);
        mDotSize = (int) typedArray.getDimension(R.styleable.BannerView_dotSize,dp2px(2));
        mDotDistance = (int) typedArray.getDimension(R.styleable.BannerView_dotDistance,dp2px(2));
        mFocusDotDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if(mFocusDotDrawable == null){
            //若没有配置，取默认值
            mFocusDotDrawable = new ColorDrawable(Color.RED);
        }
        mNormalDotDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if(mNormalDotDrawable == null){
            mNormalDotDrawable = new ColorDrawable(Color.WHITE);
        }
        mBottomColor = typedArray.getColor(R.styleable.BannerView_bottomColor,DEFAULT_COLOR);
        mWidthPercent = typedArray.getFloat(R.styleable.BannerView_widthPercent,0);
        mHeightPercent = typedArray.getFloat(R.styleable.BannerView_heightPercent,0);
        mIntervalTime = typedArray.getInt(R.styleable.BannerView_intervalTime,DEFAULT_INTERVAL_TIME);
        mSwitchTime = typedArray.getInt(R.styleable.BannerView_switchTime,DEFAULT_SWITCH_TIME);

        //回收
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //若未定义水平垂直方向比例，则使用默认的
        if(mWidthPercent == 0 || mHeightPercent == 0){
            super.onLayout(changed,l,t,r,b);
            return;
        }
        //若定义了 则重新布局
        int widthSize = this.getMeasuredWidth();
        int heightSize = (int) (widthSize*(mHeightPercent/mWidthPercent));
        this.getLayoutParams().height = heightSize;
        super.onLayout(changed, l, t, l+widthSize, t+heightSize);
    }

    public void setAdapter(BannerAdapter bannerAdapter){
        this.mAdapter = bannerAdapter;
        mBannerVp.setAdapter(bannerAdapter);

        //初始化点的指示器
        initDotIndicator();
        mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                changeIndicatorStat(position);
            }
        });

        mBannerVp.setIntervalTime(mIntervalTime);
        mBannerVp.setScrollerDuration(mSwitchTime);

        //初始化的时候显示第一条的描述
        //mTitleTv.setText(mAdapter.getDesc(0));

    }

    /**
     * 初始化点的指示器
     */
    private void initDotIndicator() {
        //设置底部背景色
        mBottomRl.setBackgroundColor(mBottomColor);
        mDotContainerLl.setGravity(getDotGravity());
        int count = mAdapter.getCount();
        for(int i=0;i<count;i++){
            DotIndicatorView dotIndicatorView = new DotIndicatorView(mContext);
            //设置圆点的宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(mDotSize),dp2px(mDotSize));
            layoutParams.leftMargin = layoutParams.rightMargin =  dp2px(mDotDistance);
            dotIndicatorView.setLayoutParams(layoutParams);
            if(i == 0){
                dotIndicatorView.setDrawable(mFocusDotDrawable);
            }else {
                dotIndicatorView.setDrawable(mNormalDotDrawable);
            }
            mDotContainerLl.addView(dotIndicatorView);
        }
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

    private int getDotGravity(){
        switch (mDotGravity){
            case 0:
                return Gravity.CENTER;
            case -1:
                return Gravity.LEFT;
            case 1:
                return Gravity.RIGHT;
            default:
                return Gravity.CENTER;
        }
    }


    private int dp2px(int dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setScrollerDuration(int scrollerDuration) {
        mBannerVp.setScrollerDuration(scrollerDuration);
    }

    /**
     * 开始滚动
     *
     */
    public void startRoll(){
        mBannerVp.startRoll();
    }




}

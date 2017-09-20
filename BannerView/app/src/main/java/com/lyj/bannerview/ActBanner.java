package com.lyj.bannerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lyj.bannerview.Banner.BannerAdapter;
import com.lyj.bannerview.Banner.BannerView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/9/14.
 */

public class ActBanner extends Activity {

    private Button mNextBtn;

    private BannerView mBannerView;

    private List<BannerBean> mBannerBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_banner);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        initData();

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActNext.actionStart(ActBanner.this,"next");
            }
        });

    }

    private void initData() {
        mBannerBeanList = new ArrayList<>();
        mBannerBeanList.add(new BannerBean("http://pic.baike.soso.com/p/20090711/20090711100446-226055609.jpg","军人"));
        mBannerBeanList.add(new BannerBean("http://pic.baike.soso.com/p/20090711/20090711101754-314944703.jpg","顺溜"));
        mBannerBeanList.add(new BannerBean("http://www.jikedaohang.com/images/first_code.jpg","第一行代码"));

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(final int position,View convertView) {
                ImageView imageView = null;
                if(convertView != null){
                    imageView = (ImageView) convertView;
                    Log.d("BannerLog","reuse == " +imageView);
                }else {
                    imageView = new ImageView(ActBanner.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Log.d("BannerLog","create == " +imageView);
                }
                BannerBean bannerBean = mBannerBeanList.get(position);
                Glide.with(ActBanner.this).load(bannerBean.getUrl()).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActNext.actionStart(ActBanner.this,mBannerBeanList.get(position).getTitle());
                    }
                });
                return imageView;
            }

            @Override
            public int getCount() {
                return mBannerBeanList.size();
            }

            //若不需要显示title的时候，不重写此方法
            @Override
            public String getDesc(int position) {
                return mBannerBeanList.get(position).getTitle();
            }
        });
        mBannerView.startRoll();

    }

}

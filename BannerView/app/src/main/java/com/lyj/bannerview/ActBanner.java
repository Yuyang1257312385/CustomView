package com.lyj.bannerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private BannerView mBannerView;

    private List<BannerBean> mBannerBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_banner);
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        initData();

    }

    private void initData() {
        mBannerBeanList = new ArrayList<>();
        mBannerBeanList.add(new BannerBean("http://pic.baike.soso.com/p/20090711/20090711100446-226055609.jpg","军人"));
        mBannerBeanList.add(new BannerBean("http://pic.baike.soso.com/p/20090711/20090711101754-314944703.jpg","顺溜"));
        mBannerBeanList.add(new BannerBean("http://www.jikedaohang.com/images/first_code.jpg","第一行代码"));

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {

                ImageView imageView = new ImageView(ActBanner.this);
                BannerBean bannerBean = mBannerBeanList.get(position%mBannerBeanList.size());
                Glide.with(ActBanner.this).load(bannerBean.getUrl()).into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return mBannerBeanList.size();
            }

            @Override
            public String getDesc(int position) {
                return mBannerBeanList.get(position).getTitle();
            }
        });
        mBannerView.setScrollerDuration(2000);
        mBannerView.startRoll();

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ActBanner.class);
        context.startActivity(intent);
    }
}

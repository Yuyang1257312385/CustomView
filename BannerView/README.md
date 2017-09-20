## BannerView使用

- 布局中添加控件
```
<com.lyj.bannerview.Banner.BannerView
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:layout_marginTop="50dp"
        >

    </com.lyj.bannerview.Banner.BannerView>

   //下面为可自定义的属性
    //选中的指示点的颜色
    <!--app:dotIndicatorFocus="#00ff00"-->
    //未选中的指示点的颜色
    <!--app:dotIndicatorNormal="#ffffff"-->
    //指示点的大小
    <!--app:dotSize="4dp"-->
    //指示点的距离
    <!--app:dotDistance="9dp"-->
    //指示点的位置
    <!--app:dotGrivity="right"-->
    //底部的背景色
    <!--app:bottomColor="#99e1e1e1"-->
    //宽度比例
    <!--app:widthPercent="25"-->
    //高度比例 宽高同时设置，不然无效
    <!--app:heightPercent="16"-->
    //多长时间切换一张图片
    <!--app:intervalTime="10000"-->
    //图片切换时的消耗的时间
    <!--app:switchTime="20000"-->
```

- 使用
```
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
                //设置点击事件
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
        //开始滚动
        mBannerView.startRoll();
```
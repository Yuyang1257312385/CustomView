package com.lyj.banner;

/**
 * Created by yu on 2017/9/18.
 */

public class BannerBean {
    private String url;
    private String title;

    public BannerBean(){

    }


    public BannerBean(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

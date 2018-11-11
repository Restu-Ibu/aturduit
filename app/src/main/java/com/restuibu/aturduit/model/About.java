package com.restuibu.aturduit.model;

public class About {
    private Integer img;
    private String desc;
    private String url;

    public About(){

    }

    public About(Integer img, String desc, String url) {
        this.img = img;
        this.desc = desc;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Integer getImg() {
        return img;
    }

    public String getDesc() {
        return desc;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

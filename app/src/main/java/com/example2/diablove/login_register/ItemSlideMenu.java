package com.example2.diablove.login_register;

/**
 * Created by Diablove on 1/2/2016.
 */
public class ItemSlideMenu  {
    private int imgId;
    private String title;

    public ItemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

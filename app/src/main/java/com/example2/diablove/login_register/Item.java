package com.example2.diablove.login_register;

import java.io.Serializable;

/**
 * Created by Diablove on 1/5/2016.
 */
public class Item implements Serializable {
    public String  user_name, item_name,category, item_info, item_loc_latitude, item_loc_longitude ,itemPath;
    String distanceToSelf;
    private static final long serialVersionUID = 1L;

    public Item(String user_name, String item_name, String category, String item_info, String latitude, String item_loc_longitude) {
        this.user_name = user_name;
        this.item_name = item_name;
        this.category = category;
        this.item_info = item_info;
        this.item_loc_latitude = latitude;
        this.item_loc_longitude = item_loc_longitude;
        this.itemPath ="";
        this.distanceToSelf ="";
    }
}

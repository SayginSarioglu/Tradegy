package com.example2.diablove.login_register;

/**
 * Created by Diablove on 1/20/2016.
 */
public class Offer {
    public String itemName, offerBy, dateOffered, price, sellBy;

    public Offer(String itemName, String offerBy,String sellBy, String dateOffered, String price) {
        this.itemName = itemName;
        this.offerBy = offerBy;
        this.sellBy = sellBy;
        this.dateOffered = dateOffered;
        this.price = price;
    }

}

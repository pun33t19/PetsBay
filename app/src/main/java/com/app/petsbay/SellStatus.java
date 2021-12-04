package com.app.petsbay;

import java.util.ArrayList;
import java.util.List;

public class SellStatus {
    List<User> userNames=new ArrayList<>();
    static boolean sold=false;
    int position=0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }



    public SellStatus(){}

    public SellStatus(List<User> userNames) {
        this.userNames = userNames;
    }

    public List<User> getUserNames() {
        return userNames;
    }
}

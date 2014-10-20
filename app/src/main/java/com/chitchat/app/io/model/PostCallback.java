package com.chitchat.app.io.model;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostCallback extends BaseCallback {

    private ArrayList<PostData> data;
    private int total;

    public ArrayList<PostData> getData() {
        return data;
    }

    public void setData(ArrayList<PostData> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

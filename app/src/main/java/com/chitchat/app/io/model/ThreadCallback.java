package com.chitchat.app.io.model;

import com.chitchat.app.model.ThreadItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/16/2014.
 */
public class ThreadCallback extends BaseCallback {
    private ArrayList<ThreadItem> data;

    public ArrayList<ThreadItem> getData() {
        return data;
    }

    public void setData(ArrayList<ThreadItem> data) {
        this.data = data;
    }
}

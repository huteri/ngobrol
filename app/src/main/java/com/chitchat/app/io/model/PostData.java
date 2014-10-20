package com.chitchat.app.io.model;

import com.chitchat.app.model.ThreadItem;

import java.io.Serializable;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostData implements Serializable{
    private int id;
    private String text;
    private UserData user;
    private ThreadItem thread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public ThreadItem getThread() {
        return thread;
    }

    public void setThread(ThreadItem thread) {
        this.thread = thread;
    }
}

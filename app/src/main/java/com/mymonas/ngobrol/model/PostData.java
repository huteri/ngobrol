package com.mymonas.ngobrol.model;

import java.io.Serializable;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostData implements Serializable{
    private int id;
    private String text;
    private String dateCreated;
    private int isModified;
    private String timestamp;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIsModified() {
        return isModified;
    }

    public void setIsModified(int isModified) {
        this.isModified = isModified;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

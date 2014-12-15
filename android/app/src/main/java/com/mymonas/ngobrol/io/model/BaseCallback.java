package com.mymonas.ngobrol.io.model;

/**
 * Created by Huteri on 10/17/2014.
 */
public class BaseCallback {
    private int success;
    private String message;
    private int count;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

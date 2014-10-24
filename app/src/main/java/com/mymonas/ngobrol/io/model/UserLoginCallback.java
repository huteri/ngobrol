package com.mymonas.ngobrol.io.model;

import com.mymonas.ngobrol.model.UserData;

/**
 * Created by Huteri on 10/18/2014.
 */
public class UserLoginCallback extends BaseCallback{
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }


}

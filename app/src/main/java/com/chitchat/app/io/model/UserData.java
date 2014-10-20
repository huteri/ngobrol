package com.chitchat.app.io.model;

/**
 * Created by Huteri on 10/19/2014.
 */
public class UserData {
        private int id;
        private String api, username;

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.mymonas.ngobrol.io.model;

/**
 * Created by Huteri on 10/19/2014.
 */
public class UserData {
        private int id;
        private String api, username, fullname, profileUrl, profileBg, aboutMe;

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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getProfileBg() {
        return profileBg;
    }

    public void setProfileBg(String profileBg) {
        this.profileBg = profileBg;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}

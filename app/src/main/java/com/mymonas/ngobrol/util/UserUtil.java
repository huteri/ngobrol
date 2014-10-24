package com.mymonas.ngobrol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.mymonas.ngobrol.model.UserData;

/**
 * Created by Huteri on 10/17/2014.
 */
public class UserUtil {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_API = "api";
    private static final String ARG_USERID = "userId";
    private static final String ARG_FULLNAME = "fullname";
    private static final String ARG_PROFILEURL = "profileUrl";
    private static final String ARG_PROFILEBG = "profileBg";
    private static final String ARG_ABOUTME = "aboutMe";

    private final Context mContext;
    private final SharedPreferences mPrefs;

    public UserUtil(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getUsername() {
        return mPrefs.getString(ARG_USERNAME, "");
    }

    public String getAPI() {
        return mPrefs.getString(ARG_API, "");
    }

    public int getUserId() {
        return mPrefs.getInt(ARG_USERID, 0);
    }

    public String getFullName() {
        return mPrefs.getString(ARG_FULLNAME, "");
    }
    
    public String getProfileUrl() {
        return mPrefs.getString(ARG_PROFILEURL, "");
    }
    
    public String getProfileBg() {
        return mPrefs.getString(ARG_PROFILEBG, "");
    }

    public String getAboutMe() {
        return mPrefs.getString(ARG_ABOUTME, "");
    }
    public boolean isAvailable() {
        return (mPrefs.getInt(ARG_USERID, 0) > 0) ? true : false;
    }
    

    public String getAndroidId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public UserData getUserData() {
        UserData data = new UserData();
        data.setAboutMe(getAboutMe());
        data.setFullname(getFullName());
        data.setId(getUserId());
        data.setProfileBg(getProfileBg());
        data.setProfileUrl(getProfileUrl());
        data.setUsername(getUsername());
        return data;
    }

    public void clear() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(ARG_API);
        editor.remove(ARG_USERNAME);
        editor.remove(ARG_USERID);

        editor.commit();

    }
}

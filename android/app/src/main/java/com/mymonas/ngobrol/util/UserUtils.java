package com.mymonas.ngobrol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.mymonas.ngobrol.model.UserData;

/**
 * Created by Huteri on 10/17/2014.
 */
public class UserUtils {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_API = "api";
    private static final String ARG_USERID = "userId";
    private static final String ARG_FULLNAME = "fullname";
    private static final String ARG_PROFILEURL = "profileUrl";
    private static final String ARG_PROFILEBG = "profileBg";
    private static final String ARG_ABOUTME = "aboutMe";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ISMODERATOR = "isModerator";


    private final Context mContext;
    private final SharedPreferences mPrefs;

    public UserUtils(Context context) {
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

    public String getEmail() {
        return mPrefs.getString(ARG_EMAIL, "");
    }

    public boolean isModerator() {
        return (mPrefs.getInt(ARG_ISMODERATOR, 0) == 1) ? true:false;
    }
    public UserData getUserData() {
        UserData data = new UserData();
        data.setApi(getAPI());
        data.setAboutMe(getAboutMe());
        data.setFullname(getFullName());
        data.setEmail(getEmail());
        data.setId(getUserId());
        data.setProfileBg(getProfileBg());
        data.setProfileUrl(getProfileUrl());
        data.setUsername(getUsername());
        data.setIsModerator(mPrefs.getInt(ARG_ISMODERATOR, 0));
        return data;
    }

    public void clear() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(ARG_API);
        editor.remove(ARG_USERNAME);
        editor.remove(ARG_USERID);
        editor.remove(ARG_ABOUTME);
        editor.remove(ARG_FULLNAME);
        editor.remove(ARG_PROFILEBG);
        editor.remove(ARG_PROFILEURL);
        editor.remove(ARG_ISMODERATOR);
        editor.remove(ARG_EMAIL);
        editor.commit();

    }

   public void saveUserData(UserData user) {
       SharedPreferences.Editor edit = mPrefs.edit();
       edit.putString(ARG_API, user.getApi());
       edit.putString(ARG_ABOUTME, user.getAboutMe());
       edit.putString(ARG_EMAIL, user.getEmail());
       edit.putString(ARG_FULLNAME, user.getFullname());
       edit.putInt(ARG_USERID, user.getId());
       edit.putString(ARG_PROFILEURL, user.getProfileUrl());
       edit.putString(ARG_PROFILEBG, user.getProfileBg());
       edit.putString(ARG_USERNAME, user.getUsername());
       edit.putInt(ARG_ISMODERATOR, user.getIsModerator());
       edit.commit();
   }

}

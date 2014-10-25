package com.mymonas.ngobrol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mymonas.ngobrol.Config;

/**
 * Created by Huteri on 10/25/2014.
 */

public class PrefUtils {
    private static final String PREF_NUM_POSTS_PER_PAGE = "pref_num_posts_per_page";

    public static int getNumPostsPerPage(final Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREF_NUM_POSTS_PER_PAGE, Config.DEFAULT_NUM_POST_PER_PAGE);
    }
}

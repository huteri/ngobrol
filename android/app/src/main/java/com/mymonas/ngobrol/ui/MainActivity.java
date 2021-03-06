package com.mymonas.ngobrol.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.makeramen.RoundedImageView;
import com.mymonas.ngobrol.Config;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.adapter.DrawerListAdapter;
import com.mymonas.ngobrol.io.RestCallback;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.model.DrawerListItem;
import com.mymonas.ngobrol.ui.profile.ProfileActivity;
import com.mymonas.ngobrol.ui.settings.LoginActivity;
import com.mymonas.ngobrol.ui.settings.SettingsActivity;
import com.mymonas.ngobrol.util.Clog;
import com.mymonas.ngobrol.util.UserUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends FragmentActivity {
    private static final int DRAWER_MY_PROFILE = 1;
    private static final int DRAWER_SETTINGS = 4;
    private static final int DRAWER_LOGIN = 5;
    private static final int DRAWER_LOGOUT = 6;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<DrawerListItem> mDrawerList;
    private DrawerListAdapter drawerAdapter;
    private RelativeLayout mNavLayout;
    private UserUtils mUserUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Clog.d("");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(!Clog.isDebugable())
            Crashlytics.start(this);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavLayout = (RelativeLayout) findViewById(R.id.nav_layout);
        mDrawerListView = (ListView) findViewById(R.id.nav_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = new ArrayList<DrawerListItem>();
        drawerAdapter = new DrawerListAdapter(MainActivity.this, mDrawerList);

        int[] iconResources = new int[10];
        String[] mDrawerTitles = new String[10];

        iconResources[DRAWER_SETTINGS] = R.drawable.ic_drawer_setting;
        mDrawerTitles[DRAWER_SETTINGS] = getString(R.string.general_settings);

        TextView mAccountDetail = (TextView) findViewById(R.id.account_detail);
        mUserUtils = new UserUtils(this);

        if (mUserUtils.isAvailable()) {
            mAccountDetail.setText(mUserUtils.getUsername());
            iconResources[DRAWER_MY_PROFILE] = R.drawable.ic_drawer_profile;
            mDrawerTitles[DRAWER_MY_PROFILE] = getString(R.string.menu_my_profile);
            iconResources[DRAWER_LOGOUT] = R.drawable.ic_drawer_logout;
            mDrawerTitles[DRAWER_LOGOUT] = getString(R.string.general_logout);
        } else {
            mAccountDetail.setText(getString(R.string.general_noaccount));
            iconResources[DRAWER_LOGIN] = R.drawable.ic_drawer_login;
            mDrawerTitles[DRAWER_LOGIN] = getString(R.string.general_signin);
        }

        for (int i = 0; i < mDrawerTitles.length; i++) {
            if (mDrawerTitles[i] != null) {
                DrawerListItem d = new DrawerListItem();
                d.setId(i);
                d.setTitle(mDrawerTitles[i]);
                d.setRes(iconResources[i]);
                mDrawerList.add(d);
            }
        }

        mDrawerListView.setAdapter(drawerAdapter);
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (getFragmentManager().findFragmentByTag("fragment") == null) {
            ThreadFragment fragment = new ThreadFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment, "fragment").commit();
        }

        RoundedImageView profileImg = (RoundedImageView) findViewById(R.id.profile_img);
        ImageView profileBg = (ImageView) findViewById(R.id.profile_bg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProfileActivity();
            }
        });

        initImageLoader();

        ImageLoader.getInstance().displayImage(mUserUtils.getProfileUrl(), profileImg);
        ImageLoader.getInstance().displayImage(mUserUtils.getProfileBg(), profileBg, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.profile_bg)
                .showImageOnFail(R.drawable.profile_bg)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build());
    }

    private void initImageLoader() {
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration.Builder iml = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileCount(Config.IMAGE_LOADER_DISK_CACHE_FILE_COUNT)
                .defaultDisplayImageOptions(imageOptions);

        if(Clog.isDebugable())
            iml.writeDebugLogs();

       ImageLoader.getInstance().init(iml.build());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if(!mUserUtils.isAvailable()) {
            MenuItem item = menu.findItem(R.id.action_new);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch(item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(this, AddEditThreadActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int pos) {

        switch (mDrawerList.get(pos).getId()) {
            case DRAWER_MY_PROFILE:
                startProfileActivity();
                break;
            case DRAWER_SETTINGS:
                startSettingsActivity();
                break;
            case DRAWER_LOGIN:
                doLoginTask();
                break;
            case DRAWER_LOGOUT:
                doLogoutTask();
                break;

        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_EXTRA_USER_DATA, mUserUtils.getUserData());
        startActivity(intent);
    }

    private void doLoginTask() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void doLogoutTask() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.general_logout));
        pDialog.setMessage(getString(R.string.logout_progress));
        pDialog.show();

        RestClient.get().logoutUser(mUserUtils.getUserId(), mUserUtils.getAPI(), new RestCallback<BaseCallback>(this) {
            @Override
            public void success(BaseCallback baseCallback, Response response) {
                super.success(baseCallback, response);
                pDialog.dismiss();
                if (baseCallback.getSuccess() == 1) {
                    mUserUtils.clear();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                pDialog.dismiss();
            }
        });

    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
            mDrawerLayout.closeDrawer(mNavLayout);
        }
    }
}

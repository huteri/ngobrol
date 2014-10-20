package com.mymonas.ngobrol.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.ui.adapter.DrawerListAdapter;
import com.mymonas.ngobrol.ui.model.DrawerListItem;
import com.mymonas.ngobrol.util.Clog;
import com.mymonas.ngobrol.util.UserUtil;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends FragmentActivity {
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<DrawerListItem> drawerList;
    private DrawerListAdapter drawerAdapter;
    private RelativeLayout mNavLayout;
    private UserUtil mUserUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Clog.d("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavLayout = (RelativeLayout) findViewById(R.id.nav_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);
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

        drawerList = new ArrayList<DrawerListItem>();
        drawerAdapter = new DrawerListAdapter(MainActivity.this, drawerList);

        int[] iconResources = {R.drawable.ic_drawer_home, 0};
        String[] mDrawerTitles = {"Home", ""};

        TextView mAccountDetail = (TextView) findViewById(R.id.account_detail);
        mUserUtil = new UserUtil(this);

        if (mUserUtil.isAvailable()) {
            mAccountDetail.setText(mUserUtil.getUsername());
            iconResources[1] = R.drawable.ic_drawer_logout;
            mDrawerTitles[1] = getString(R.string.general_logout);
        } else {
            mAccountDetail.setText(getString(R.string.general_noaccount));
            iconResources[1] = R.drawable.ic_drawer_login;
            mDrawerTitles[1] = getString(R.string.general_signin);
        }

        for (int i = 0; i < mDrawerTitles.length; i++) {
            DrawerListItem d = new DrawerListItem();
            d.setTitle(mDrawerTitles[i]);
            d.setRes(iconResources[i]);
            drawerList.add(d);
        }

        mDrawerList.setAdapter(drawerAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (getFragmentManager().findFragmentByTag("fragment") == null) {
            ThreadFragment fragment = new ThreadFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment, "fragment").commit();
        }

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int pos) {

        switch (pos) {
            case 1:
                if (mUserUtil.isAvailable()) {
                    doLogoutTask();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
        }
    }

    private void doLogoutTask() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.general_logout));
        pDialog.setMessage(getString(R.string.logout_progress));
        pDialog.show();

        RestClient.get().logoutUser(mUserUtil.getUserId(), mUserUtil.getAPI(), new Callback<BaseCallback>() {
            @Override
            public void success(BaseCallback baseCallback, Response response) {
                pDialog.dismiss();
                if (baseCallback.getSuccess() == 1) {
                    mUserUtil.clear();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
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

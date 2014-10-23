package com.mymonas.ngobrol.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ProfileActivity extends Activity {

    private UserUtil mUserUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_profile);

        Drawable actionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab_background_textured_blue);
        actionBarBackgroundDrawable.setAlpha(0);

        getActionBar().setBackgroundDrawable(actionBarBackgroundDrawable);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mUserUtil = new UserUtil(this);

        getActionBar().setTitle(mUserUtil.getUsername() + getString(R.string.profile_title));

        TextView tvName = (TextView) findViewById(R.id.name);
        TextView tvAboutMe = (TextView) findViewById(R.id.about_me_text);
        ImageView profilebg = (ImageView) findViewById(R.id.profile_bg);
        RoundedImageView profileImg = (RoundedImageView) findViewById(R.id.profile_img);


        if (mUserUtil.getFullName().length() > 0) {
            tvName.setText(mUserUtil.getFullName());
        } else
            tvName.setText(mUserUtil.getUsername());

        tvAboutMe.setText(mUserUtil.getAboutMe());

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader.init(imageLoaderConfiguration);
        imageLoader.displayImage(mUserUtil.getProfileUrl(), profileImg);
        imageLoader.displayImage(mUserUtil.getProfileBg(), profilebg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

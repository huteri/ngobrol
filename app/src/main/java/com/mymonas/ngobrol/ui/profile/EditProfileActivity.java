package com.mymonas.ngobrol.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.mymonas.ngobrol.Config;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.model.UserData;
import com.mymonas.ngobrol.util.Clog;
import com.mymonas.ngobrol.util.FileUtils;
import com.mymonas.ngobrol.util.ImageUtils;
import com.mymonas.ngobrol.util.UserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by Huteri on 11/2/2014.
 */
public class EditProfileActivity extends Activity {

    private static final int REQ_UPLOAD_PROFILE_PIC = 100;
    private static final String PROFILE_PIC_FILENAME = "profile_pic.png";
    private static final int REQ_UPLOAD_PROFILE_BACKGROUND = 101;
    private static final String PROFILE_BG_FILENAME = "profile_bg.png";
    private static final int TYPE_PROFILE_PIC = 1;
    private static final int TYPE_PROFILE_BG = 2;
    private FormEditText mEtRealName;
    private FormEditText mEtEmail;
    private EditText mEtAboutMe;
    private UserUtils mUserUtils;
    private ImageView mProfileImg;
    private ImageView mProfileBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mUserUtils = new UserUtils(this);

        mEtRealName = (FormEditText) findViewById(R.id.et_real_name);
        mEtEmail = (FormEditText) findViewById(R.id.et_email);
        mEtAboutMe = (EditText) findViewById(R.id.et_about_me);

        // initialize

        mEtRealName.setText(mUserUtils.getFullName());
        mEtEmail.setText(mUserUtils.getEmail());
        mEtAboutMe.setText(mUserUtils.getAboutMe());

        mProfileImg = (ImageView) findViewById(R.id.profile_img);
        ImageLoader.getInstance().displayImage(mUserUtils.getProfileUrl(), mProfileImg);

        mProfileBg = (ImageView) findViewById(R.id.profile_bg);
        ImageLoader.getInstance().displayImage(mUserUtils.getProfileBg(), mProfileBg);

        LinearLayout profileImgLayout = (LinearLayout) findViewById(R.id.layout_profle_img);
        profileImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfilePic();
            }
        });

        LinearLayout bgImgLayout = (LinearLayout) findViewById(R.id.layout_profile_bg);
        bgImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBackgroundPic();
            }
        });
    }

    private void uploadBackgroundPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQ_UPLOAD_PROFILE_BACKGROUND);
    }

    private void uploadProfilePic() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQ_UPLOAD_PROFILE_PIC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                saveProfile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_UPLOAD_PROFILE_PIC) {
            if (resultCode == RESULT_OK) {

                Uri selectedImageUri = data.getData();
                String selectedImagePath = FileUtils.getPath(this, selectedImageUri);

                Clog.d("selectedImagePath: " + selectedImagePath);
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                Bitmap newBitmap = ImageUtils.getResizedBitmap(bitmap, Config.PROFILE_PIC_MAX_WIDTH, Config.PROFILE_PIC_MAX_HEIGHT);
                File photo = ImageUtils.compressBitmapToFile(newBitmap, PROFILE_PIC_FILENAME);
                TypedFile typedFile = new TypedFile("image/*", photo);

                final ProgressDialog pDialog = getUploadingProgressDialog();
                pDialog.show();

                RestClient.get().uploadPic(mUserUtils.getAPI(), mUserUtils.getUserId(),TYPE_PROFILE_PIC, typedFile, new Callback<BaseCallback>() {
                    @Override
                    public void success(BaseCallback baseCallback, Response response) {
                        pDialog.dismiss();
                        if (baseCallback.getSuccess() == 1) {
                            UserData user = mUserUtils.getUserData();
                            MemoryCacheUtils.removeFromCache(user.getProfileUrl(), ImageLoader.getInstance().getMemoryCache());
                            DiskCacheUtils.removeFromCache(user.getProfileUrl(), ImageLoader.getInstance().getDiskCache());

                            ImageLoader.getInstance().displayImage(baseCallback.getMessage(), mProfileImg);

                            user.setProfileUrl(baseCallback.getMessage());
                            mUserUtils.saveUserData(user);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();
                    }
                });

            }
        } else if (requestCode == REQ_UPLOAD_PROFILE_BACKGROUND) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = FileUtils.getPath(this, selectedImageUri);

                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                Bitmap newBitmap = ImageUtils.getResizedBitmap(bitmap, Config.PROFILE_BG_MAX_WIDTH, Config.PROFILE_BG_MAX_HEIGHT);
                TypedFile file = new TypedFile("image/*", ImageUtils.compressBitmapToFile(newBitmap, PROFILE_BG_FILENAME));

                final ProgressDialog pDialog = getUploadingProgressDialog();
                pDialog.show();

                RestClient.get().uploadPic(mUserUtils.getAPI(), mUserUtils.getUserId(), TYPE_PROFILE_BG, file, new Callback<BaseCallback>() {
                    @Override
                    public void success(BaseCallback baseCallback, Response response) {
                        pDialog.dismiss();
                        if(baseCallback.getSuccess() == 1) {
                            UserData user = mUserUtils.getUserData();
                            MemoryCacheUtils.removeFromCache(user.getProfileBg(), ImageLoader.getInstance().getMemoryCache());
                            DiskCacheUtils.removeFromCache(user.getProfileBg(), ImageLoader.getInstance().getDiskCache());

                            ImageLoader.getInstance().displayImage(baseCallback.getMessage(), mProfileBg);

                            user.setProfileBg(baseCallback.getMessage());
                            mUserUtils.saveUserData(user);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();
                    }
                });

            }
        }
    }


    private ProgressDialog getUploadingProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.profile_edit_dialog_upload_title));
        dialog.setMessage(getString(R.string.profile_edit_dialog_upload_message));
        dialog.setCancelable(false);
        return dialog;
    }

    private void saveProfile() {
        if (isAllDataValid()) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setTitle(getString(R.string.profile_edit_dialog_title));
            pDialog.setMessage(getString(R.string.profile_edit_dialog_message));
            pDialog.setCancelable(false);
            pDialog.show();

            RestClient.get().editProfile(mUserUtils.getUserId(), mUserUtils.getAPI(), mUserUtils.getAndroidId(), mEtRealName.getText().toString(), mEtEmail.getText().toString(), mEtAboutMe.getText().toString(), new Callback<BaseCallback>() {
                @Override
                public void success(BaseCallback baseCallback, Response response) {
                    pDialog.dismiss();
                    if (baseCallback.getSuccess() == 1) {
                        saveProfileToPrefs();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, baseCallback.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                private void saveProfileToPrefs() {
                    UserData user = mUserUtils.getUserData();
                    user.setEmail(mEtEmail.getText().toString());
                    user.setFullname(mEtRealName.getText().toString());
                    user.setAboutMe(mEtAboutMe.getText().toString());

                    mUserUtils.saveUserData(user);
                }

                @Override
                public void failure(RetrofitError error) {
                    pDialog.dismiss();
                }
            });

        }
    }

    private boolean isAllDataValid() {
        return mEtRealName.testValidity() && mEtEmail.testValidity();

    }
}

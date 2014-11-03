package com.mymonas.ngobrol.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.mymonas.ngobrol.util.ImageUtils;
import com.mymonas.ngobrol.util.UserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by Huteri on 11/2/2014.
 */
public class EditProfileActivity extends Activity {

    private static final int REQ_UPLOAD_PROFILE_PIC = 100;
    private FormEditText mEtRealName;
    private FormEditText mEtEmail;
    private EditText mEtAboutMe;
    private UserUtils mUserUtils;
    private ImageView mProfileImg;

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

        LinearLayout profileImgLayout = (LinearLayout) findViewById(R.id.layout_profle_img);
        profileImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfilePic();
            }
        });
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

    private void uploadProfilePic() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQ_UPLOAD_PROFILE_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_UPLOAD_PROFILE_PIC) {
            if (resultCode == RESULT_OK) {
                String selectedImagePath;
                Uri selectedImageUri = data.getData();

                if(Build.VERSION.SDK_INT >= 19) {
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // Check for the freshest data.
                    getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                }
                Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
                if (cursor == null) {
                    selectedImagePath = selectedImageUri.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    selectedImagePath = cursor.getString(idx);
                }

                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                Bitmap newBitmap = ImageUtils.getResizedBitmap(bitmap, Config.PROFILE_PIC_MAX_WIDTH, Config.PROFILE_PIC_MAX_HEIGHT);

                File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                File photo = new File(filePath, "profilePic.png");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(photo);
                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TypedFile typedFile = new TypedFile("image/*", photo);


                final ProgressDialog pDialog = getUploadingProgressDialog();
                pDialog.show();

                RestClient.get().uploadPic(mUserUtils.getAPI(), mUserUtils.getUserId(), typedFile, new Callback<BaseCallback>() {
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
        }
    }

    private ProgressDialog getUploadingProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait...");
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

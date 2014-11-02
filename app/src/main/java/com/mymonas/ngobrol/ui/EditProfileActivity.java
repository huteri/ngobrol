package com.mymonas.ngobrol.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.model.UserData;
import com.mymonas.ngobrol.util.UserUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 11/2/2014.
 */
public class EditProfileActivity extends Activity {

    private FormEditText mEtRealName;
    private FormEditText mEtEmail;
    private EditText mEtAboutMe;
    private UserUtils mUserUtils;

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
        if (mEtRealName.testValidity() && mEtEmail.testValidity())
            return true;

        return false;

    }
}

package com.mymonas.ngobrol.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.micromobs.android.floatlabel.FloatLabelEditText;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestCallback;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.UserLoginCallback;
import com.mymonas.ngobrol.ui.MainActivity;
import com.mymonas.ngobrol.util.UserUtils;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {

    private static final int BTN_START_PROGRESS = 1;
    private static final int BTN_NORMAL = 0;
    private static final int BTN_SUCCESS = 100;
    private FloatLabelEditText mEtAccountName;
    private FloatLabelEditText mEtAccountPass;
    private ActionProcessButton mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mEtAccountName = (FloatLabelEditText) findViewById(R.id.accountName);
        mEtAccountPass = (FloatLabelEditText) findViewById(R.id.accountPassword);
        mBtnSubmit = (ActionProcessButton) findViewById(R.id.submit);

        mBtnSubmit.setMode(ActionProcessButton.Mode.ENDLESS);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        Button btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    private void submit() {
        final String userEmail = mEtAccountName.getText().toString();
        final String userPass = mEtAccountPass.getText().toString();

        if(userEmail.length()<1) {
            mEtAccountName.getFloatingLabel().setText(getString(R.string.register_error_username_required));
            mEtAccountName.getFloatingLabel().setTextColor(Color.RED);
            mEtAccountName.setIsCustomText(true);
            mEtAccountName.showFloatingLabel();
            return;
        } else if(userPass.length() < 1) {
            mEtAccountPass.getFloatingLabel().setText(getString(R.string.register_error_password_required));
            mEtAccountPass.getFloatingLabel().setTextColor(Color.RED);
            mEtAccountPass.setIsCustomText(true);
            mEtAccountPass.showFloatingLabel();
            return;
        }

        doLoginTask(userEmail, userPass);
    }

    private void doLoginTask(final String userName, String userPass) {

        String androidName = Build.MODEL;
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mEtAccountName.setEnabled(false);
        mEtAccountPass.setEnabled(false);
        mBtnSubmit.setProgress(BTN_START_PROGRESS);

        RestClient.get().logUser(userName, userPass, androidName, androidId, new RestCallback<UserLoginCallback>(this) {
            @Override
            public void success(UserLoginCallback userLoginCallback, Response response) {
                super.success(userLoginCallback, response);
               if(userLoginCallback.getSuccess() == 1) {
                   mBtnSubmit.setProgress(BTN_SUCCESS);

                   UserUtils userUtils = new UserUtils(LoginActivity.this);
                   userUtils.saveUserData(userLoginCallback.getData());

                   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                   startActivity(intent);
                   LoginActivity.this.finish();

               } else {
                   Toast.makeText(LoginActivity.this, userLoginCallback.getMessage(), Toast.LENGTH_SHORT).show();
                   mBtnSubmit.setProgress(BTN_NORMAL);
               }

            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                mBtnSubmit.setProgress(BTN_NORMAL);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

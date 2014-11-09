package com.mymonas.ngobrol.ui.settings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.micromobs.android.floatlabel.FloatLabelEditText;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestCallback;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends Activity {

    private FloatLabelEditText mEtUsername;
    private FloatLabelEditText mEtPassword;
    private FloatLabelEditText mEtRePassword;
    private ActionProcessButton mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mEtUsername = (FloatLabelEditText) findViewById(R.id.accountName);
        mEtPassword = (FloatLabelEditText) findViewById(R.id.accountPassword);
        mEtRePassword = (FloatLabelEditText) findViewById(R.id.accountRePassword);

        mBtnRegister = (ActionProcessButton) findViewById(R.id.submit);
        mBtnRegister.setMode(ActionProcessButton.Mode.ENDLESS);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });

    }

    private void registerAccount() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        String repassword = mEtRePassword.getText().toString();

        if(TextUtils.isEmpty(username)) {
            mEtUsername.getFloatingLabel().setText(getString(R.string.register_error_username_required));
            mEtUsername.getFloatingLabel().setTextColor(Color.RED);
            mEtUsername.setIsCustomText(true);
            mEtUsername.showFloatingLabel();
            return;
        } else if(TextUtils.isEmpty(password)) {
            mEtPassword.getFloatingLabel().setText(getString(R.string.register_error_password_required));
            mEtPassword.getFloatingLabel().setTextColor(Color.RED);
            mEtPassword.setIsCustomText(true);
            mEtPassword.showFloatingLabel();
            return;
        } else if(!password.contentEquals(repassword)) {
            mEtPassword.getFloatingLabel().setText(getString(R.string.register_error_confirm_password));
            mEtPassword.getFloatingLabel().setTextColor(Color.RED);
            mEtPassword.setIsCustomText(true);
            mEtPassword.showFloatingLabel();

            mEtRePassword.getFloatingLabel().setText(getString(R.string.register_error_confirm_password));
            mEtRePassword.getFloatingLabel().setTextColor(Color.RED);
            mEtRePassword.setIsCustomText(true);
            mEtRePassword.showFloatingLabel();

            return;
        }

        doRegisterTask(username, password);

    }

    private void doRegisterTask(String username, String password) {

        mBtnRegister.setProgress(1);

        RestClient.get().regUser(username, password, new RestCallback<BaseCallback>(this) {
            @Override
            public void success(BaseCallback baseCallback, Response response) {
                super.success(baseCallback, response);
                if(baseCallback.getSuccess() == 1) {
                    mBtnRegister.setProgress(100);
                    Toast.makeText(RegisterActivity.this,getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                } else {
                    mBtnRegister.setProgress(0);
                    Toast.makeText(RegisterActivity.this, baseCallback.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                mBtnRegister.setProgress(0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

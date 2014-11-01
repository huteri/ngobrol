package com.mymonas.ngobrol.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.io.model.CategoryCallback;
import com.mymonas.ngobrol.model.CategoryItem;
import com.mymonas.ngobrol.ui.adapter.SpinnerCategoryAdapter;
import com.mymonas.ngobrol.util.UserUtils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 10/30/2014.
 */
public class AddEditThreadActivity extends Activity {

    private boolean mIsEditMode = false;
    private FormEditText mEtTitle;
    private FormEditText mEtPost;
    private Spinner mSpCategory;
    private ArrayList<CategoryItem> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mIsEditMode) {
            final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int layout;
            layout = R.layout.actionbar_custom_view_done_discard;

            final View customActionBarView = inflater.inflate(layout, null);

            customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(validateData()) {
                       final ProgressDialog pDialog = new ProgressDialog(AddEditThreadActivity.this);
                       pDialog.setTitle(getString(R.string.new_thread_done_title));
                       pDialog.setMessage(getString(R.string.new_thread_done_message));
                       pDialog.show();
                       UserUtils user = new UserUtils(AddEditThreadActivity.this);

                       RestClient.get().submitThread(user.getUserId(), user.getAPI(), user.getAndroidId(), mCategoryList.get(mSpCategory.getSelectedItemPosition()).getId(),
                               mEtTitle.getText().toString(), mEtPost.getText().toString(), new Callback<BaseCallback>() {
                                   @Override
                                   public void success(BaseCallback baseCallback, Response response) {
                                       pDialog.dismiss();
                                       if(baseCallback.getSuccess() == 1) {
                                           Intent intent = new Intent(AddEditThreadActivity.this, MainActivity.class);
                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                           startActivity(intent);
                                           AddEditThreadActivity.this.finish();
                                       } else {
                                           Toast.makeText(AddEditThreadActivity.this, baseCallback.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   }

                                   @Override
                                   public void failure(RetrofitError error) {
                                        pDialog.dismiss();
                                   }
                               });

                   }
                }

                private boolean validateData() {
                    if(mEtTitle.testValidity() && mEtPost.testValidity()) {
                        return true;
                    }
                    return false;
                }
            });

            customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            final ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

            actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_add_edit_thread);

        mEtTitle = (FormEditText) findViewById(R.id.et_title);
        mEtPost = (FormEditText) findViewById(R.id.et_post);
        mSpCategory = (Spinner) findViewById(R.id.sp_category);
        TextView tvPoster = (TextView) findViewById(R.id.posted_as);

        UserUtils userUtils = new UserUtils(this);
        tvPoster.setText(getString(R.string.new_thread_poster)+" "+userUtils.getUsername());

        mCategoryList = new ArrayList<CategoryItem>();
        final SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, mCategoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpCategory.setAdapter(categoryAdapter);

        final ProgressBar pbCategory = (ProgressBar) findViewById(R.id.pb_category);
        pbCategory.setVisibility(View.VISIBLE);
        RestClient.get().getCategories(new Callback<CategoryCallback>() {
            @Override
            public void success(CategoryCallback categoryCallback, Response response) {
                pbCategory.setVisibility(View.GONE);
                if (categoryCallback.getSuccess() == 1) {
                    mCategoryList.clear();
                    mCategoryList.addAll(categoryCallback.getData());
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}

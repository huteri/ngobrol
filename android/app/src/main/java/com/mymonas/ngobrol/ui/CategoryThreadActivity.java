package com.mymonas.ngobrol.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.CategoryItem;
import com.mymonas.ngobrol.util.UserUtils;

/**
 * Created by Huteri on 10/30/2014.
 */
public class CategoryThreadActivity extends FragmentActivity {

    public static final String KEY_EXTRA_CATEGORY_DATA = "category_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_thread);

        CategoryItem categoryItem = (CategoryItem) getIntent().getSerializableExtra(KEY_EXTRA_CATEGORY_DATA);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(categoryItem.getColor())));
        getActionBar().setTitle(categoryItem.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportFragmentManager().findFragmentByTag("fragment") == null) {
            Bundle args = new Bundle();
            args.putSerializable(KEY_EXTRA_CATEGORY_DATA, categoryItem);

            ThreadFragment fragment = new ThreadFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment, "fragment").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        UserUtils userUtils = new UserUtils(this);
        if(!userUtils.isAvailable()) {
            MenuItem item = menu.findItem(R.id.action_new);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_new:
                Intent intent = new Intent(this, AddEditThreadActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}

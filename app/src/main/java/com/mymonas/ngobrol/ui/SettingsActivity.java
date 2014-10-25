package com.mymonas.ngobrol.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.mymonas.ngobrol.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.pref_general);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    
}

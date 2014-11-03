package com.mymonas.ngobrol.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.ui.widget.NumberPickerPreference;
import com.mymonas.ngobrol.util.PrefUtils;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private NumberPickerPreference mPrefNumPostPerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.pref_general);

        mPrefNumPostPerPage = (NumberPickerPreference) findPreference(PrefUtils.PREF_NUM_POSTS_PER_PAGE);
        mPrefNumPostPerPage.setSummary(String.valueOf(PrefUtils.getNumPostsPerPage(this)));

        Preference about = (Preference) findPreference("pref_about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.contentEquals(PrefUtils.PREF_NUM_POSTS_PER_PAGE)) {
            mPrefNumPostPerPage.setSummary(String.valueOf(PrefUtils.getNumPostsPerPage(this)));
        }
    }
}

package com.ikotori.coolweather.about;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.util.ActivityUtils;

public class AboutActivity extends AppCompatActivity {

    private TextView mVersionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.pref_title_about);
        mVersionView = findViewById(R.id.about_version);
        mVersionView.setText(String.format("%sV%s",
                getString(R.string.about_current_version),
                ActivityUtils.getVersionName(getApplicationContext())));
        Button userAgreement = findViewById(R.id.about_user_agreement);
        userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserAgreementDialog();
            }
        });
    }

    private void showUserAgreementDialog() {
        final Dialog dialog = new Dialog(AboutActivity.this);
        dialog.setContentView(R.layout.dialog_user_agreement);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

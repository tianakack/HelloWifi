package com.superball.hellowifi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.TextView;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ///
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        ///
        try {

            TextView appVersion = (TextView) findViewById(R.id.appVersion);

            appVersion.setText(getString(R.string.about_version, getPackageManager().getPackageInfo(getPackageName(), 0).versionName));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

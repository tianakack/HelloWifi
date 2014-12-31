package com.superball.hellowifi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ScanListFragment.OnFragmentInteractionListener {

    boolean mPendingExit = false;
    android.os.Handler mHandler = null;
    Runnable mClearPendingExitRunnable = null;

    public void onFragmentInteraction(int id) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ScanListFragment.newInstance("", ""))
                    .commit();
        }

        mPendingExit = false;
        mHandler = new android.os.Handler();
        mClearPendingExitRunnable = new Runnable() {
            @Override
            public void run() {
                mPendingExit = false;
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ScanListFragment scanListFragment = (ScanListFragment) getFragmentManager().findFragmentById(R.id.container);

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_reload: {
                scanListFragment.reload();
            }
            return true;

            case R.id.action_menu_sort_by_ssid: {
                scanListFragment.sort_by_ssid();
            }
            return true;

            case R.id.action_menu_sort_by_bssid: {
                scanListFragment.sort_by_bssid();
            }
            return true;

            case R.id.action_menu_sort_by_rssi: {
                scanListFragment.sort_by_rssi();
            }
            return true;

            case R.id.action_menu_sort_by_frequency: {
                scanListFragment.sort_by_frequency();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!mPendingExit) {

            mPendingExit = true;

            mHandler.removeCallbacks(mClearPendingExitRunnable);
            mHandler.postDelayed(mClearPendingExitRunnable, 2500);

            Toast.makeText(MainActivity.this, R.string.pending_exit, Toast.LENGTH_SHORT).show();

        } else {

            super.onBackPressed();
        }
    }
}

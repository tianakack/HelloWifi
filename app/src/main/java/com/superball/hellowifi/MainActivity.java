package com.superball.hellowifi;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.superball.hellowifi.ScanList.ScanListFragment;
import com.superball.hellowifi.Spectrogram.SpectrogramFragment;


public class MainActivity extends ActionBarActivity implements BlankFragment.OnFragmentInteractionListener, ScanListFragment.OnFragmentInteractionListener, SpectrogramFragment.OnFragmentInteractionListener {

    ///
    Fragment mFragment = null;
    ///
    BlankFragment mBlankFragment = null;
    ScanListFragment mScanListFragment = null;
    SpectrogramFragment mSpectrogramFragment = null;
    ///
    android.os.Handler mHandler = null;
    ///
    boolean mPendingExit = false;
    Runnable mClearPendingExitRunnable = null;
    ///
    boolean mWifiEnabled = false;
    Runnable mCheckWifiStatusRunnable = null;

    ///
    public void onFragmentInteraction(Uri uri) {

    }

    public void onFragmentInteraction(int id) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///
        mBlankFragment = BlankFragment.newInstance("", "");
        mFragment = mScanListFragment = ScanListFragment.newInstance("", "");
        mSpectrogramFragment = SpectrogramFragment.newInstance("", "");

        ///
        mHandler = new android.os.Handler();

        ///
        mPendingExit = false;
        mClearPendingExitRunnable = new Runnable() {
            @Override
            public void run() {
                mPendingExit = false;
            }
        };

        ///
        mCheckWifiStatusRunnable = new Runnable() {
            @Override
            public void run() {

                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                if (wifiManager.isWifiEnabled()) {

                    if (!mWifiEnabled) {

                        mWifiEnabled = true;

                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, mFragment)
                                .commit();
                    }

                } else {

                    if (mWifiEnabled) {

                        mWifiEnabled = false;

                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, mBlankFragment)
                                .commit();
                    }
                }

                ///
                mHandler.removeCallbacks(mCheckWifiStatusRunnable);
                mHandler.postDelayed(mCheckWifiStatusRunnable, 2500);
            }
        };

        ///
        mHandler.removeCallbacks(mCheckWifiStatusRunnable);
        mHandler.post(mCheckWifiStatusRunnable);

        ///
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mBlankFragment)
                    .commit();
        }

        ///
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.actionbar_items, android.R.layout.simple_spinner_dropdown_item);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                switch (itemPosition) {

                    case 0: {
                        mFragment = mScanListFragment;
                    }
                    break;

                    case 1: {
                        mFragment = mSpectrogramFragment;
                    }
                    break;
                }

                if (mWifiEnabled) {

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, mFragment)
                            .commit();

                } else {

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, mBlankFragment)
                            .commit();
                }

                return false;
            }

        });

        ///
        OUIHelper.createInstance(MainActivity.this);
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

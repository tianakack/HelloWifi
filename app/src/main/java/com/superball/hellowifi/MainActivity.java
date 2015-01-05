package com.superball.hellowifi;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements BlankFragment.OnFragmentInteractionListener, ScanListFragment.OnFragmentInteractionListener {

    ///
    BlankFragment mBlankFragment = null;
    ScanListFragment mScanListFragment = null;
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
        mScanListFragment = ScanListFragment.newInstance("", "");

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
                                .replace(R.id.container, mScanListFragment)
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

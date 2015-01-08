package com.superball.hellowifi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.superball.hellowifi.Detail.DetailActivity;
import com.superball.hellowifi.ScanList.ScanListFragment;
import com.superball.hellowifi.Spectrogram.SpectrogramFragment;


public class MainActivity extends ActionBarActivity implements BlankFragment.OnFragmentInteractionListener, ScanListFragment.OnFragmentInteractionListener, SpectrogramFragment.OnFragmentInteractionListener {

    ///
    final static String HELLOWIFI_SCANRESULT = "com.superball.hellowifi.scanresult";
    final static String ACTIONBAR_LIST_ITEM_POSITION = "actionbar.list.item_position";

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

    public void onScanListItemClick(ScanResult scanResult) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        intent.putExtra(HELLOWIFI_SCANRESULT, scanResult);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ///
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

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
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mBlankFragment)
                    .commit();
        } else {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(ACTIONBAR_LIST_ITEM_POSITION));
        }

        ///
        OUIHelper.createInstance(MainActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        final ActionBar actionBar = getSupportActionBar();

        outState.putInt(ACTIONBAR_LIST_ITEM_POSITION, actionBar.getSelectedNavigationIndex());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        ///
        mHandler.removeCallbacks(mCheckWifiStatusRunnable);
        mHandler.post(mCheckWifiStatusRunnable);

        super.onResume();
    }

    @Override
    protected void onPause() {

        ///
        mHandler.removeCallbacks(mCheckWifiStatusRunnable);

        super.onPause();
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

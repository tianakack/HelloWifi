package com.superball.hellowifi.Detail;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.superball.hellowifi.MainActivity;
import com.superball.hellowifi.OUIHelper;
import com.superball.hellowifi.R;
import com.superball.hellowifi.TimerHelper;


public class DetailActivity extends ActionBarActivity {

    private final String TAG = this.getClass().getSimpleName();

    private TimerHelper mTimerHelper = null;

    private SignalView mSignalView = null;

    private ScanResult mScanResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ///
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        ///
        mSignalView = (SignalView) findViewById(R.id.signal_view);

        ///
        mScanResult = getIntent().getParcelableExtra(MainActivity.HELLOWIFI_SCANRESULT);

        ///
        getSupportActionBar().setTitle(mScanResult.SSID);

        ///
        TextView text_info = (TextView) findViewById(R.id.detail_info);

        text_info.setText(
                String.format("%.3f GHz\n%s\n%s",
                        mScanResult.frequency / 1000.0,
                        mScanResult.BSSID.toUpperCase(),
                        mScanResult.capabilities)
        );

        ///
        TextView text_org = (TextView) findViewById(R.id.detail_org);

        text_org.setText(OUIHelper.getORG(mScanResult.BSSID.toUpperCase().substring(0, 8)));

        ///
        mTimerHelper = new TimerHelper() {
            @Override
            public void run() {

                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                if (wifiManager.isWifiEnabled()) {

                    rescan();
                    mSignalView.postInvalidate();

                } else {

                    finish();
                }
            }
        };
    }

    @Override
    protected void onResume() {

        mSignalView.addSignalData(SignalView.MIN_VALUE);

        mTimerHelper.start(500, 5000);

        super.onResume();
    }

    @Override
    protected void onPause() {

        mTimerHelper.stop();

        mSignalView.addSignalData(SignalView.MIN_VALUE);

        super.onPause();
    }

    public void rescan() {

        ///
        ScanResult scanResult = null;

        ///
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        for (ScanResult result : wifiManager.getScanResults()) {

            if (result.BSSID.compareTo(mScanResult.BSSID) == 0) {

                scanResult = result;
            }
        }

        ///
        if (scanResult != null) {

            mSignalView.addSignalData(scanResult.level);

        } else {

            mSignalView.addSignalData(SignalView.MIN_VALUE);
        }

        ///
        wifiManager.startScan();
    }
}

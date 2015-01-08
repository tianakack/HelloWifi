package com.superball.hellowifi.ScanList;

import android.net.wifi.ScanResult;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class ScanItem {

    public ScanResult scanResult;

    public ScanItem(ScanResult result) {
        scanResult = result;
    }

    public static ScanItem new_instance(ScanResult result) {
        return new ScanItem(result);
    }

    @Override
    public String toString() {
        return scanResult.SSID;
    }
}

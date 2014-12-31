package com.superball.hellowifi.scan;

import android.net.wifi.ScanResult;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ScanList {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static SparseArray<DummyItem> ITEM_MAP = new SparseArray<DummyItem>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.idx, item);
    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void rearrange() {

        Collections.sort(ITEMS, new Comparator<DummyItem>() {
            @Override
            public int compare(DummyItem lhs, DummyItem rhs) {
                return lhs.content.SSID.compareTo(rhs.content.SSID);
            }
        });
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {

        private static int global_idx = 0;

        public int idx;
        public ScanResult content;

        public DummyItem(ScanResult content) {
            this.idx = ++global_idx;
            this.content = content;
        }

        @Override
        public String toString() {
            return content.SSID;
        }
    }
}

package com.superball.hellowifi.scan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by TIAN on 2014/12/31.
 */
public class ScanListAdapter extends ArrayAdapter<ScanList.DummyItem> {

    public ScanListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ScanListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ScanListAdapter(Context context, int resource, ScanList.DummyItem[] objects) {
        super(context, resource, objects);
    }

    public ScanListAdapter(Context context, int resource, int textViewResourceId, ScanList.DummyItem[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ScanListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public ScanListAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}

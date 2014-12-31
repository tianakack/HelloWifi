package com.superball.hellowifi.scan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.superball.hellowifi.R;

import java.util.List;

/**
 * Created by TIAN on 2014/12/31.
 */
public class ScanListAdapter extends ArrayAdapter<ScanList.ScanItem> {

    public ScanListAdapter(Context context, int resource, int textViewResourceId, List<ScanList.ScanItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        if (view instanceof RelativeLayout) {

            ScanList.ScanItem item = getItem(position);

            ///
            ImageView image_signal = (ImageView) view.findViewById(R.id.scan_item_signal);

            if (item.content.level >= -50) {

                image_signal.setImageResource(R.drawable.signal100);

            } else if (item.content.level >= -75) {

                image_signal.setImageResource(R.drawable.signal75);

            } else if (item.content.level >= -100) {

                image_signal.setImageResource(R.drawable.signal50);

            } else {

                image_signal.setImageResource(R.drawable.signal0);
            }

            ///
            TextView text = (TextView) view.findViewById(R.id.scan_item_detail);

            text.setText(
                    String.format("信号: %d dBm\n" +
                                    "频率: %.3f GHz\n" +
                                    "BSSID: %s\n" +
                                    "%s",
                            item.content.level,
                            item.content.frequency / 1000.0,
                            item.content.BSSID.toUpperCase(),
                            item.content.capabilities
                    )
            );

            ///
            ImageView image_security = (ImageView) view.findViewById(R.id.scan_item_security);

            String capabilities = item.content.capabilities.toUpperCase();

            if (capabilities.contains("WPA") || capabilities.contains("WEP") || capabilities.contains("WAPI")) {

                image_security.setVisibility(View.VISIBLE);

            } else {

                image_security.setVisibility(View.GONE);
            }
        }

        return view;
    }
}

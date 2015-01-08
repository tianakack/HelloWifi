package com.superball.hellowifi.ScanList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superball.hellowifi.OUIHelper;
import com.superball.hellowifi.R;

import java.util.List;

/**
 * Created by TIAN on 2014/12/31.
 */
public class ScanListAdapter extends ArrayAdapter<ScanItem> {

    private Context mContext;

    public ScanListAdapter(Context context, int resource, int textViewResourceId, List<ScanItem> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        if (view instanceof LinearLayout) {

            ScanItem item = getItem(position);

            ///
            ImageView image_signal = (ImageView) view.findViewById(R.id.scan_item_signal);

            if (item.scanResult.level > -50) {

                image_signal.setImageResource(R.drawable.signal100);

            } else if (item.scanResult.level > -65) {

                image_signal.setImageResource(R.drawable.signal75);

            } else if (item.scanResult.level > -80) {

                image_signal.setImageResource(R.drawable.signal50);

            } else {

                image_signal.setImageResource(R.drawable.signal0);
            }

            ///
            TextView text_detail = (TextView) view.findViewById(R.id.scan_item_detail);

            text_detail.setText(
                    mContext.getString(R.string.scan_item_detail,
                            item.scanResult.BSSID.toUpperCase(),
                            item.scanResult.level,
                            item.scanResult.frequency / 1000.0,
                            item.scanResult.capabilities
                    )
            );

            ///
            ImageView image_security = (ImageView) view.findViewById(R.id.scan_item_security);

            String capabilities = item.scanResult.capabilities.toUpperCase();

            if (capabilities.contains("WPA") || capabilities.contains("WEP") || capabilities.contains("WAPI")) {

                image_security.setVisibility(View.VISIBLE);

            } else {

                image_security.setVisibility(View.GONE);
            }

            ///
            TextView text_org = (TextView) view.findViewById(R.id.scan_item_org);

            text_org.setText(OUIHelper.getORG(item.scanResult.BSSID.toUpperCase().substring(0, 8)));
        }

        return view;
    }
}

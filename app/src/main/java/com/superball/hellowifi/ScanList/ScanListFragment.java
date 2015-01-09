package com.superball.hellowifi.ScanList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.superball.hellowifi.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ScanListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * An array of sample (dummy) items.
     */
    public List<ScanItem> mScanList;
    SORT_TYPE mSortType = SORT_TYPE.RSSI;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ScanListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScanListFragment() {
    }

    // TODO: Rename and change types of parameters
    public static ScanListFragment newInstance(String param1, String param2) {
        ScanListFragment fragment = new ScanListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your scanResult

        /**
         * An array of sample (dummy) items.
         */
        mScanList = new ArrayList<>();
        mAdapter = new ScanListAdapter(getActivity(), R.layout.scan_list_item, R.id.scan_item_ssid, mScanList);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {

        rescan();
        sort(mSortType);

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_frag_scanlist, menu);

        switch (mSortType) {

            case SSID:
                menu.findItem(R.id.action_sort_by_ssid).setChecked(true);
                break;

            case BSSID:
                menu.findItem(R.id.action_sort_by_bssid).setChecked(true);
                break;

            case RSSI:
                menu.findItem(R.id.action_sort_by_rssi).setChecked(true);
                break;

            case Frequency:
                menu.findItem(R.id.action_sort_by_frequency).setChecked(true);
                break;

            case Capacities:
                menu.findItem(R.id.action_sort_by_capabilities).setChecked(true);
                break;
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isCheckable()) {
            item.setChecked(true);
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_rescan: {
                rescan();
                sort(mSortType);
            }
            return true;

            case R.id.action_sort_by_ssid: {
                mSortType = SORT_TYPE.SSID;
                sort(mSortType);
            }
            return true;

            case R.id.action_sort_by_bssid: {
                mSortType = SORT_TYPE.BSSID;
                sort(mSortType);
            }
            return true;

            case R.id.action_sort_by_rssi: {
                mSortType = SORT_TYPE.RSSI;
                sort(mSortType);
            }
            return true;

            case R.id.action_sort_by_frequency: {
                mSortType = SORT_TYPE.Frequency;
                sort(mSortType);
            }
            return true;

            case R.id.action_sort_by_capabilities: {
                mSortType = SORT_TYPE.Capacities;
                sort(mSortType);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onScanListItemClick(mScanList.get(position).scanResult);
        }
    }

    /**
     * The default scanResult for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void rescan() {

        Activity activity = getActivity();

        if (activity == null) return;

        ///
        mScanList.clear();

        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);

        for (ScanResult scanResult : wifiManager.getScanResults()) {
            mScanList.add(ScanItem.new_instance(scanResult));
        }

        ///
        wifiManager.startScan();
    }

    public void sort(SORT_TYPE sortType) {

        switch (sortType) {

            case SSID: {

                mAdapter.sort(new Comparator<ScanItem>() {

                    @Override
                    public int compare(ScanItem lhs, ScanItem rhs) {

                        return lhs.scanResult.SSID.compareTo(rhs.scanResult.SSID);
                    }
                });
            }
            break;

            case BSSID: {

                mAdapter.sort(new Comparator<ScanItem>() {

                    @Override
                    public int compare(ScanItem lhs, ScanItem rhs) {

                        return lhs.scanResult.BSSID.compareTo(rhs.scanResult.BSSID);
                    }
                });
            }
            break;

            case RSSI: {

                mAdapter.sort(new Comparator<ScanItem>() {

                    @Override
                    public int compare(ScanItem lhs, ScanItem rhs) {

                        return rhs.scanResult.level - lhs.scanResult.level;
                    }
                });
            }
            break;

            case Frequency: {

                mAdapter.sort(new Comparator<ScanItem>() {

                    @Override
                    public int compare(ScanItem lhs, ScanItem rhs) {

                        return lhs.scanResult.frequency - rhs.scanResult.frequency;
                    }
                });
            }
            break;

            case Capacities: {

                mAdapter.sort(new Comparator<ScanItem>() {

                    @Override
                    public int compare(ScanItem lhs, ScanItem rhs) {

                        return lhs.scanResult.capabilities.compareTo(rhs.scanResult.capabilities);
                    }
                });
            }
            break;
        }

        mAdapter.notifyDataSetChanged();
    }

    public enum SORT_TYPE {
        SSID,
        BSSID,
        RSSI,
        Frequency,
        Capacities
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onScanListItemClick(ScanResult scanResult);
    }

}

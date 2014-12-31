package com.superball.hellowifi;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ScanListFragment.OnFragmentInteractionListener{

    ScanListFragment mScanListFragment = null;

    public void onFragmentInteraction(int id)
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanListFragment = ScanListFragment.newInstance("", "");
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mScanListFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_reload:
            {
                mScanListFragment.reload();
            }
            return true;

            case R.id.action_menu:
            {

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

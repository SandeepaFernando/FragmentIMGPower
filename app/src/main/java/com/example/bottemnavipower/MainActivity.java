package com.example.bottemnavipower;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new CabinFragment(), "CABIN");
        fragmentTransaction.commit();

        actionModeCallback = new ActionModeCallback();


    }

    public void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);

    }

    private void toggleSelection(int position) {
        CabinFragment.mMediaStoreAdapter.toggleSelection(position);
        int count = CabinFragment.mMediaStoreAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    public void enableActionModeForSite(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelectionForSite(position);
    }

    private void toggleSelectionForSite(int position) {
        SiteFragment.msiteMeadiaAdapter.toggleSelection(position);
        int count = SiteFragment.msiteMeadiaAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    public void enableActionModeForTower(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelectionForTower(position);
    }

    private void toggleSelectionForTower(int position) {
        TowerFragment.mtowerMeadiaAdapter.toggleSelection(position);
        int count = TowerFragment.mtowerMeadiaAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cabin:
                    FragmentTransaction fragmentTransactionCabin = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionCabin.replace(R.id.fragment_container, new CabinFragment(), "CABIN");
                    fragmentTransactionCabin.commit();
                    Log.i("TAB", "CabinFragment");
                    return true;
                case R.id.navigation_site:
                    FragmentTransaction fragmentTransactionSite = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSite.replace(R.id.fragment_container, new SiteFragment(), "SITE");
                    fragmentTransactionSite.commit();
                    CabinFragment.mMediaStoreAdapter.notifyDataSetChanged();
                    Log.i("TAB", "SiteFragment");
                    return true;
                case R.id.navigation_tower:
                    FragmentTransaction fragmentTransactionTower = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionTower.replace(R.id.fragment_container, new TowerFragment(), "TOWER");
                    fragmentTransactionTower.commit();
                    Log.i("TAB", "TowerFragment");
                    return true;
            }
            return false;
        }
    };


    public class ActionModeCallback implements ActionMode.Callback {


        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Log.i("TEST", "====================Testing===================");
            Tools.setSystemBarColor(MainActivity.this, R.color.colorAccent);
            actionMode.getMenuInflater().inflate(R.menu.menu_upload, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.action_upload) {
                //uploadIMGs();
                actionMode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            CabinFragment cabinFragment = (CabinFragment) getSupportFragmentManager().findFragmentByTag("CABIN");
            if (cabinFragment != null && cabinFragment.isVisible()) {
                // add your code here
                Log.i("TEST", "CabinFragmentonDestroyActionMode");
                CabinFragment.mMediaStoreAdapter.clearSelections();
            }

            SiteFragment siteFragment = (SiteFragment) getSupportFragmentManager().findFragmentByTag("SITE");
            if (siteFragment != null && siteFragment.isVisible()) {
                SiteFragment.msiteMeadiaAdapter.clearSelections();
                Log.i("TEST", "SiteFragmentonDestroyActionMode");
            }

            TowerFragment towerFragment = (TowerFragment) getSupportFragmentManager().findFragmentByTag("TOWER");
            if (towerFragment != null && towerFragment.isVisible()){
                TowerFragment.mtowerMeadiaAdapter.clearSelections();
            }

            actionMode = null;
            Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimary);

        }
    }


}

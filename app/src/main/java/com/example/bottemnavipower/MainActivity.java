package com.example.bottemnavipower;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private String TAG_CABIN = "CABIN";
    private String TAG_SITE = "SITE";
    private String TAG_TOWER = "TOWER";
    private ArrayList<String> uriArray;
    private static String URL = "http://intern1.telco.lk/mapp/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        String tag = intent.getStringExtra("EXTRAFragmet");
        if (tag != null) {

            Log.i("TAG", tag);

            switch (tag) {
                case "CABIN":
                    FragmentTransaction fragmentTransactionCabin = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionCabin.add(R.id.fragment_container, new CabinFragment(), TAG_CABIN);
                    fragmentTransactionCabin.commit();
                    Log.i("TAG","CABIN Fragment");
                    navView.setSelectedItemId(R.id.navigation_cabin);
                    break;

                case "SITE":
                    FragmentTransaction fragmentTransactionSite = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSite.add(R.id.fragment_container, new SiteFragment(), TAG_SITE);
                    fragmentTransactionSite.commit();
                    Log.i("TAG","SITE Fragment");
                    navView.setSelectedItemId(R.id.navigation_site);
                    break;

                case "TOWER":
                    FragmentTransaction fragmentTransactionTower = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionTower.add(R.id.fragment_container, new TowerFragment(), TAG_TOWER);
                    fragmentTransactionTower.commit();
                    Log.i("TAG","TOWER Fragment");
                    navView.setSelectedItemId(R.id.navigation_tower);
                    break;
            }

        } else {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new CabinFragment(), TAG_CABIN);
            fragmentTransaction.commit();
            Log.i("TAG", "Main Frag");

        }

        actionModeCallback = new ActionModeCallback();

        uriArray = new ArrayList<>();
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
            Log.i("TAG", "onNavigationItemSelected");
            switch (item.getItemId()) {
                case R.id.navigation_cabin:
                    FragmentTransaction fragmentTransactionCabin = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionCabin.replace(R.id.fragment_container, new CabinFragment(), TAG_CABIN);
                    fragmentTransactionCabin.commit();
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    return true;

                case R.id.navigation_site:
                    FragmentTransaction fragmentTransactionSite = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSite.replace(R.id.fragment_container, new SiteFragment(), TAG_SITE);
                    fragmentTransactionSite.commit();
                    CabinFragment.mMediaStoreAdapter.notifyDataSetChanged();
                    Log.i("TAB", "SiteFragment");
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    return true;

                case R.id.navigation_tower:
                    FragmentTransaction fragmentTransactionTower = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionTower.replace(R.id.fragment_container, new TowerFragment(), TAG_TOWER);
                    fragmentTransactionTower.commit();
                    Log.i("TAB", "TowerFragment");
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    return true;
            }
            return false;
        }
    };


    public class ActionModeCallback implements ActionMode.Callback {


        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Tools.setSystemBarColor(MainActivity.this, R.color.colorAccent);
            actionMode.getMenuInflater().inflate(R.menu.menu_upload, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.action_upload) {
                //upload Button click
                //CabinFragment.mMediaStoreAdapter.getSelectedUri();
                Log.i("UPLOAD", CabinFragment.mMediaStoreAdapter.getUris().toString());
                //uriArray.add(Arrays.toString(CabinFragment.mMediaStoreAdapter.getUris().values().toArray()));


                Iterator myVeryOwnIterator = CabinFragment.mMediaStoreAdapter.getUris().keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    Object key=myVeryOwnIterator.next();
                    //String value=(String)CabinFragment.mMediaStoreAdapter.getUris().get(key);
                    uriArray.add(CabinFragment.mMediaStoreAdapter.getUris().get(key));
                    //Toast.makeText(ctx, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
                }
                Log.i("UPLOADARRY", String.valueOf(uriArray));

                for (int i = 0; i < uriArray.size(); i++){
                    uploadMultipart(uriArray.get(i));
                }

                actionMode.finish();
                uriArray.clear();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            CabinFragment cabinFragment = (CabinFragment) getSupportFragmentManager().findFragmentByTag(TAG_CABIN);
            if (cabinFragment != null && cabinFragment.isVisible()) {
                // add your code here
                Log.i("TEST", "CabinFragmentonDestroyActionMode");
                CabinFragment.mMediaStoreAdapter.clearSelections();
            }

            SiteFragment siteFragment = (SiteFragment) getSupportFragmentManager().findFragmentByTag(TAG_SITE);
            if (siteFragment != null && siteFragment.isVisible()) {
                SiteFragment.msiteMeadiaAdapter.clearSelections();
                Log.i("TEST", "SiteFragmentonDestroyActionMode");
            }

            TowerFragment towerFragment = (TowerFragment) getSupportFragmentManager().findFragmentByTag(TAG_TOWER);
            if (towerFragment != null && towerFragment.isVisible()) {
                TowerFragment.mtowerMeadiaAdapter.clearSelections();
            }
            actionMode = null;
            Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimary);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart(String filePath) {
        //String caption = etCaption.getText().toString().trim();

        //getting the actual path of the image
        Log.i("PATH", filePath);
        //Date caption = new Date();

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, URL)
                    .addFileToUpload(filePath, "imagefile") //Adding file
                    .addParameter("site_id", "CM0003") //Adding text parameter to the request
                    .addParameter("image_type", "testing")
                    .addParameter("security_token", "aaaaaaaaaaaabbbbbbbbb")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("Exception", exc.getMessage());
        }
    }


}

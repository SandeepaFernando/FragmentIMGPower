package com.example.bottemnavipower;

import android.content.Context;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private String TAG_CABIN = "CABIN";
    private String TAG_SITE = "SITE";
    private String TAG_TOWER = "TOWER";
    private ArrayList<String> uriArrayCabin;
    private ArrayList<String> uriArraySite;
    private ArrayList<String> uriArrayTower;
    private static String URL = "http://intern1.telco.lk/mapp/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.example.bottemnavipower";

        Intent intent = getIntent();
        String tag = intent.getStringExtra("EXTRAFragmet");
        if (tag != null) {

            Log.i("TAG", tag);

            switch (tag) {
                case "CABIN":
                    FragmentTransaction fragmentTransactionCabin = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionCabin.add(R.id.fragment_container, new CabinFragment(), TAG_CABIN);
                    fragmentTransactionCabin.commit();
                    Log.i("TAG", "CABIN Fragment");
                    navView.setSelectedItemId(R.id.navigation_cabin);
                    break;

                case "SITE":
                    FragmentTransaction fragmentTransactionSite = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSite.add(R.id.fragment_container, new SiteFragment(), TAG_SITE);
                    fragmentTransactionSite.commit();
                    Log.i("TAG", "SITE Fragment");
                    navView.setSelectedItemId(R.id.navigation_site);
                    break;

                case "TOWER":
                    FragmentTransaction fragmentTransactionTower = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionTower.add(R.id.fragment_container, new TowerFragment(), TAG_TOWER);
                    fragmentTransactionTower.commit();
                    Log.i("TAG", "TOWER Fragment");
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

        uriArrayCabin = new ArrayList<>();
        uriArraySite = new ArrayList<>();
        uriArrayTower = new ArrayList<>();
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
                CabinFragment cabinFragment = (CabinFragment) getSupportFragmentManager().findFragmentByTag(TAG_CABIN);
                if (cabinFragment != null && cabinFragment.isVisible()) {
                    Iterator IteratorCabin = CabinFragment.mMediaStoreAdapter.getUris().keySet().iterator();
                    while (IteratorCabin.hasNext()) {
                        Object key = IteratorCabin.next();
                        uriArrayCabin.add(CabinFragment.mMediaStoreAdapter.getUris().get(key));
                    }
                    Log.i("CABINURI", uriArrayCabin.toString());

                    for (int i = 0; i < uriArrayCabin.size(); i++) {
                        uploadMultipart(uriArrayCabin.get(i), "cabin");
                    }
                }

                SiteFragment siteFragment = (SiteFragment) getSupportFragmentManager().findFragmentByTag(TAG_SITE);
                if (siteFragment != null && siteFragment.isVisible()) {
                    Iterator IteratorSite = SiteFragment.msiteMeadiaAdapter.getUris().keySet().iterator();
                    while (IteratorSite.hasNext()) {
                        Object key = IteratorSite.next();
                        uriArraySite.add(SiteFragment.msiteMeadiaAdapter.getUris().get(key));
                    }
                    Log.i("SITEURI", uriArraySite.toString());

                    for (int i = 0; i < uriArraySite.size(); i++) {
                        uploadMultipart(uriArraySite.get(i), "site");
                    }
                }

                TowerFragment towerFragment = (TowerFragment) getSupportFragmentManager().findFragmentByTag(TAG_TOWER);
                if (towerFragment != null && towerFragment.isVisible()) {
                    Iterator IteratorTower = TowerFragment.mtowerMeadiaAdapter.getUris().keySet().iterator();
                    while (IteratorTower.hasNext()) {
                        Object key = IteratorTower.next();
                        uriArrayTower.add(TowerFragment.mtowerMeadiaAdapter.getUris().get(key));
                    }
                    Log.i("TOWERURI", uriArrayTower.toString());

                    for (int i = 0; i < uriArrayTower.size(); i++) {
                        uploadMultipart(uriArrayTower.get(i), "tower");
                    }
                }

                actionMode.finish();
//
//                if (!uriArrayCabin.isEmpty()) {
//                    uriArrayCabin.clear();
//                }
//                if (!uriArraySite.isEmpty()){
//                    uriArraySite.clear();
//                }
//                if (!uriArrayTower.isEmpty()){
//                    uriArrayTower.clear();
//                }
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
                uriArrayCabin.clear();
            }

            SiteFragment siteFragment = (SiteFragment) getSupportFragmentManager().findFragmentByTag(TAG_SITE);
            if (siteFragment != null && siteFragment.isVisible()) {
                SiteFragment.msiteMeadiaAdapter.clearSelections();
                Log.i("TEST", "SiteFragmentonDestroyActionMode");
                uriArraySite.clear();
            }

            TowerFragment towerFragment = (TowerFragment) getSupportFragmentManager().findFragmentByTag(TAG_TOWER);
            if (towerFragment != null && towerFragment.isVisible()) {
                TowerFragment.mtowerMeadiaAdapter.clearSelections();
                uriArrayTower.clear();
            }
            actionMode = null;
            Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimary);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart(final String filePath, final String img_type) {
        Log.i("PATH", filePath);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    new MultipartUploadRequest(MainActivity.this, URL)
                            .addFileToUpload(filePath, "imagefile")
                            .addParameter("site_id", "sdfs")
                            .addParameter("image_type", img_type)
                            .addParameter("security_token", "sfgrg")
                            .setUtf8Charset()
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    Log.i("UPLOAD", String.valueOf(uploadInfo.getUploadedBytes()));
                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                    Log.e("UPLOAD", "Error", exception);
                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Log.i("UPLOAD", serverResponse.toString());
                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                    Log.i("UPLOAD", uploadInfo.toString());
                                }
                            })
                            .setMethod("POST")
                            .startUpload();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }


}

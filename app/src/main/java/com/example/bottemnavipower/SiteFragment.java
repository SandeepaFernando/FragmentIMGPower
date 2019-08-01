package com.example.bottemnavipower;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Objects;


public class SiteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int PERMMISSION_RESULT = 1;
    private final static int MEDIASTORE_LOADER_ID = 0;
    //public static MediaStoreAdapter mMediaStoreAdapter;
    public static SiteMeadiaAdapter msiteMeadiaAdapter;
    private RecyclerView mThumbnailRecyclerView;
    android.hardware.Camera camera;
    FrameLayout camFrame;
    ShowLivePreviewCameraWindow showCameraWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site, container, false);

        camFrame = view.findViewById(R.id.camera_view);

        mThumbnailRecyclerView = view.findViewById(R.id.thumbnailRecyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mThumbnailRecyclerView.setLayoutManager(gridLayoutManager);
//        mMediaStoreAdapter = new MediaStoreAdapter(getActivity());
//        mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);

        msiteMeadiaAdapter = new SiteMeadiaAdapter(getActivity());
        mThumbnailRecyclerView.setAdapter(msiteMeadiaAdapter);

//        mMediaStoreAdapter.setOnClickListener(new MediaStoreAdapter.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onItemClick(View view, int pos) {
//                if (mMediaStoreAdapter.getSelectedItemCount() > 0) {
//                    //enableActionMode(pos);
//                    ((MainActivity) Objects.requireNonNull(getActivity())).enableActionModeForSite(pos);
//                    Log.i("LONGCLICK", "clickedAfterLong");
//                } else {
//                    // read the inbox which removes bold from the row
//                    Log.i("CLICK", "clicked");
//
//                }
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void OnItemLongClick(View view, Uri uri, int position) {
//                Log.i("LONGCLIK", "long clicked");
//                //enableActionMode(position);
//                ((MainActivity) Objects.requireNonNull(getActivity())).enableActionModeForSite(position);
//            }
//        });

        msiteMeadiaAdapter.setOnClickListener(new SiteMeadiaAdapter.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(View view, int pos) {
                if (msiteMeadiaAdapter.getSelectedItemCount() > 0) {
                    //enableActionMode(pos);
                    ((MainActivity) Objects.requireNonNull(getActivity())).enableActionModeForSite(pos);
                    Log.i("LONGCLICK", "clickedAfterLong");
                } else {
                    // read the inbox which removes bold from the row
                    Log.i("CLICK", "clicked");

                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void OnItemLongClick(View view, Uri uri, int position) {
                ((MainActivity) Objects.requireNonNull(getActivity())).enableActionModeForSite(position);
            }
        });

        verifyUserPermission();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.i("CAMERAPERMISSION", "Granted");

            camFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent camintent = new Intent(getContext(), CameraActivity.class);
                    startActivity(camintent);
                    //camera.stopPreview();
                    //camera.release();
                }
            });
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){

            verifyUserPermission();

        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            camera_window();
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                LoaderManager.getInstance(Objects.requireNonNull(getActivity())).initLoader(MEDIASTORE_LOADER_ID, null, this);
            }
        }

    }

    private void verifyUserPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    permissions[0]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    permissions[1]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    permissions[2]) == PackageManager.PERMISSION_GRANTED){


                LoaderManager.getInstance(Objects.requireNonNull(getActivity())).initLoader(MEDIASTORE_LOADER_ID, null, this);
                //getLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
                camera_window();

            } else {
                Toast.makeText(getContext(), "Permision Denyed", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, PERMMISSION_RESULT);
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

        return new CursorLoader(
                getContext(),
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        msiteMeadiaAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        msiteMeadiaAdapter.changeCursor(null);
    }

    public void camera_window() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCameraWindow = new ShowLivePreviewCameraWindow(getContext(), camera);
            camFrame.addView(showCameraWindow);
        }
    }


}


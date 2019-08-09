package com.example.bottemnavipower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    Button bnCamera;
    ImageView previewImage;
    Uri image;
    String mCameraFileName;
    String path = "/sdcard/PowerApp/";
    private String TAG_CABIN = "CABIN";
    private String TAG_SITE = "SITE";
    private String TAG_TOWER = "TOWER";
    private String EXTRA_TAG = "EXTRAFragmet";
    private static String URL = "http://intern1.telco.lk/mapp/upload.php";
    private String fragment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        bnCamera = findViewById(R.id.camera_button);
        previewImage = findViewById(R.id.preview);

        cameraIntent();
        createdirectory();

        Intent intent = getIntent();
        String tag = intent.getStringExtra("EXTRA_CABIN");
        Log.i("CAMERA", tag);
        switch (tag) {
            case "CABIN":
                fragment_type = "cabin";
                break;

            case "SITE":
                fragment_type = "site";
                break;

            case "TOWER":
                fragment_type = "tower";
                break;

        }

    }

    public void createdirectory() {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            Log.i("DIRCREATING", "Folder created");

        }
    }

    private void cameraIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        String newPicFile = date + ".jpg";
        String outPath = path + newPicFile;
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            if (data != null) {
                image = data.getData();
                previewImage.setImageURI(image);
                previewImage.setVisibility(View.VISIBLE);
            }
            if (image == null && mCameraFileName != null) {
                image = Uri.fromFile(new File(mCameraFileName));
                previewImage.setImageURI(image);
                previewImage.setVisibility(View.VISIBLE);
            }

            bnCamera.setText(R.string.upload_bn);
            Log.i("CAMERAURI", mCameraFileName);
            bnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadData(mCameraFileName, fragment_type);
                }
            });

        } else {
            Intent intent = getIntent();
            String tag = intent.getStringExtra("EXTRA_CABIN");
            Log.i("CAMERA", tag);
            Intent mainactivityIntent = new Intent(CameraActivity.this, MainActivity.class);

            switch (tag) {
                case "CABIN":
                    mainactivityIntent.putExtra(EXTRA_TAG, TAG_CABIN);
                    break;

                case "SITE":
                    mainactivityIntent.putExtra(EXTRA_TAG, TAG_SITE);
                    break;

                case "TOWER":
                    mainactivityIntent.putExtra(EXTRA_TAG, TAG_TOWER);
                    break;

            }
            startActivity(mainactivityIntent);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadData(final String filePath, final String img_type) {
        Log.i("PATH", filePath);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    new MultipartUploadRequest(CameraActivity.this, URL)
                            .addFileToUpload(filePath, "imagefile")
                            .addParameter("site_id", "sdfs")
                            .addParameter("image_type", img_type)
                            .addParameter("security_token", "sfgrg")
                            .setUtf8Charset()
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    Log.i("UploadInfo", String.valueOf(uploadInfo.getUploadedBytes()));
                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                    Log.e("UPLOAD", "Error", exception);
                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Log.i("ServerResponse", serverResponse.toString());
                                    Intent intent = getIntent();
                                    String tag = intent.getStringExtra("EXTRA_CABIN");
                                    Log.i("CAMERA", tag);
                                    Intent mainactivityIntent = new Intent(CameraActivity.this, MainActivity.class);

                                    switch (tag) {
                                        case "CABIN":
                                            mainactivityIntent.putExtra(EXTRA_TAG, TAG_CABIN);
                                            break;

                                        case "SITE":
                                            mainactivityIntent.putExtra(EXTRA_TAG, TAG_SITE);
                                            break;

                                        case "TOWER":
                                            mainactivityIntent.putExtra(EXTRA_TAG, TAG_TOWER);
                                            break;

                                    }
                                    startActivity(mainactivityIntent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                    Log.i("UploadInfoOnCancelled", uploadInfo.toString());
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


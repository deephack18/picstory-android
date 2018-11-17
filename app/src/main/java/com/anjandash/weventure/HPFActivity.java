package com.anjandash.weventure;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjandash.weventure.restclient.RequestSender;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class HPFActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    //public static final int RESULT_OK = 0;

    Bitmap mainPhoto, jinxPhoto;
    Uri mainUri;

    RequestSender requestSender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpf);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Button button = (Button) findViewById(R.id.buttontp2);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView imv = (ImageView) findViewById(R.id.imageView);
            imv.setImageBitmap(imageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            try {
//                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
//                    mainPhoto = MediaStore.Images.Media.getBitmap(HPFActivity.this.getContentResolver(), mainUri);
//                    jinxPhoto = mainPhoto;
//
//                    //base64 encoded string
//
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    jinxPhoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                    byte[] byteArray = byteArrayOutputStream .toByteArray();
//
//                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//                    ImageView imv = (ImageView) findViewById(R.id.imageView);
//                    imv.setImageBitmap(jinxPhoto);
//
//
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

//    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
//
//    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
//            final Context context) {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        (Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//                } else {
//                    ActivityCompat
//                            .requestPermissions(
//                                    (Activity) context,
//                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
//                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//
//        } else {
//            return true;
//        }
//    }
//
//    public void showDialog(final String msg, final Context context,
//                           final String permission) {
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//        alertBuilder.setCancelable(true);
//        alertBuilder.setTitle("Permission necessary");
//        alertBuilder.setMessage(msg + " permission is necessary");
//        alertBuilder.setPositiveButton(android.R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions((Activity) context,
//                                new String[] { permission },
//                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                    }
//                });
//        AlertDialog alert = alertBuilder.create();
//        alert.show();
//    }



}

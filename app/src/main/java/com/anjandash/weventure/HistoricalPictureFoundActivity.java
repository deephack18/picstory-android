package com.anjandash.weventure;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.anjandash.weventure.restclient.model.Challenge;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;

/**
 * Created by hlib on 11/17/18.
 */

public class HistoricalPictureFoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_picture_found);

        Bitmap bitmap = getBitmapFromSharedPreferences();
        ImageView view = findViewById(R.id.hist_img);
        view.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromSharedPreferences() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("challenge", MODE_PRIVATE);
        String json = mPrefs.getString("latest_challenge", "");
        Challenge challenge = gson.fromJson(json, Challenge.class);

        byte[] bytes = Base64.decode(challenge.getImage(), 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}

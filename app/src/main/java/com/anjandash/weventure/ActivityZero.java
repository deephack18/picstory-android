package com.anjandash.weventure;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by hlib on 11/17/18.
 */

public class ActivityZero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_0);

    }

    public void moveToNextActivity(View view) {
        //TODO implement moving to next activity here
        Toast.makeText(ActivityZero.this, "dwdw", Toast.LENGTH_SHORT).show();
    }
}

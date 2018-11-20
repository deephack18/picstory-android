package com.anjandash.weventure;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        int points = intent.getIntExtra("POINTS", -1);

        if(points != -1){
            Toast.makeText(ProfileActivity.this, "You got " + points + " out of 100 points!", Toast.LENGTH_SHORT).show();
            Button pointsButton = findViewById(R.id.button);
            int currentPoints = Integer.parseInt((String)pointsButton.getText());
            pointsButton.setText(Integer.toString(currentPoints + points));
        }
    }

    @Override
    public void onBackPressed() {
        (new AlertDialog.Builder(this))
                .setTitle("Exit PicStory")
                .setMessage("Do you want to exit?")
                .setPositiveButton("NO", null)
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }
}

package com.anjandash.weventure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjandash.weventure.restclient.RequestSender;
import com.anjandash.weventure.restclient.model.Challenge;
import com.anjandash.weventure.restclient.model.ChallengeResult;
import com.anjandash.weventure.restclient.model.NewChallenge;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

/**
 * Created by hlib on 11/17/18.
 */

public class HistoricalPictureFoundActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private int challengeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_picture_found);

        final Challenge challenge = getChallengeFromSharedPreferences();
        this.challengeId = challenge.getChallengeId();
        byte[] bytes = Base64.decode(challenge.getImage(), 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        ImageView view = findViewById(R.id.hist);
        view.setImageBitmap(bitmap);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Button button = (Button) findViewById(R.id.buttontp);
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

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded_str = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Toast.makeText(HistoricalPictureFoundActivity.this, "" + this.challengeId, Toast.LENGTH_LONG).show();
            RequestSender.submitChallengePhoto(this, new NewChallenge(encoded_str, this.challengeId),
                    new RequestSender.SubmitChallengePhotoCallback() {
                        @Override
                        public void onChallengeResult(ChallengeResult challengeResult) {
                            Intent i = new Intent(
                                    HistoricalPictureFoundActivity.this,
                                    ProfileActivity.class);
                            i.putExtra("POINTS", challengeResult.getPoints());
                            startActivity(new Intent(i));
                        }
                    }
            );
        }
    }

    private Challenge getChallengeFromSharedPreferences() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("challenge", MODE_PRIVATE);
        String json = mPrefs.getString("latest_challenge", "");
        Challenge challenge = gson.fromJson(json, Challenge.class);
        return challenge;
    }
}

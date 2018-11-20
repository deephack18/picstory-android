package com.anjandash.weventure.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.anjandash.weventure.restclient.RequestSender;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by hlib on 11/20/18.
 */

public class LocationService extends JobService {

    private static final String TAG = "LocationService";

    public static int LOCATION_SERVICE_JOB_ID = 12313;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "No location permissions");
            return false;
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i(TAG,"Location: " + location);
                RequestSender.sendLocation(LocationService.this,
                        location.getLongitude(), location.getLatitude(),
                        new HistoricalPictureFoundCallback());
                jobFinished(jobParameters, true);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}

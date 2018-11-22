package com.anjandash.weventure;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.anjandash.weventure.service.LocationService;

/**
 * Created by hlib on 11/21/18.
 */

public class App extends Application {

    private final static String TAG = "App";

    private static final long INTERVAL_MILLIS = 1000 * 60 * 15;
    private static final long FLEX_MILLIS = 1000 * 60 * 5;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting location service job scheduler...");
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(LocationService.LOCATION_SERVICE_JOB_ID,
                new ComponentName(this, LocationService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(INTERVAL_MILLIS, FLEX_MILLIS)
                .build());
    }
}

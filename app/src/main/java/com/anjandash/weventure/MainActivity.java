package com.anjandash.weventure;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjandash.weventure.restclient.Intromanager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 12;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "KEY";

    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = true;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    FusedLocationProviderApi fusedLocationProviderApi;

    private ViewPager viewPager;
    private Intromanager intromanager;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    Button next,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActionBar().hide();

        intromanager = new Intromanager(this);
        if(!intromanager.Check()){
            intromanager.setFirst(false);
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
            finish();
        }

        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        skip = (Button) findViewById(R.id.btn_skip);
        next = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{R.layout.activity_screen_1, R.layout.activity_screen_2, R.layout.activity_screen_3, R.layout.activity_screen_4};

        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);


        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                int current = getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }
                else{
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new MyLocationListener();
//        try {
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                intent.putExtra("MESSAGE:", "MainAct");
//                startActivity(intent);
//
//            }
//        });


    }

    private void addBottomDots(int position){

        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0)
            dots[position].setTextColor(colorActive[position]);
    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }

        @Override
        public void onPageSelected(int position){
            addBottomDots(position);
            if(position==layouts.length-1){
                next.setText("PROCEED");
                skip.setVisibility(View.GONE);
            } else {
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state){

        }
    };



    public class ViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount(){
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object){
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            View v = (View) object;
            container.removeView(v);
        }
    }

//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location loc) {
//            TextView editLocation = (TextView) findViewById(R.id.text1);
//            editLocation.setText("");
//            //pb.setVisibility(View.INVISIBLE);
//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            String longitude = "Longitude: " + loc.getLongitude();
//            Log.v("LONG", longitude);
//            String latitude = "Latitude: " + loc.getLatitude();
//            Log.v("LAT", latitude);
//
//            /*------- To get city name from coordinates -------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    System.out.println(addresses.get(0).getLocality());
//                    cityName = addresses.get(0).getLocality();
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                    + cityName;
//            editLocation.setText(s);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {}
//
//        @Override
//        public void onProviderEnabled(String provider) {}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    }





}

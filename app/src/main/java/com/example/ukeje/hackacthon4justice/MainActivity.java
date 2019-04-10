package com.example.ukeje.hackacthon4justice;

//author = Ukeje Emeka
//Email = ukejee@3gmail.com


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final  String BASE_URL = "http://192.168.91.107:8080/";

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
  //  private MakeCallFragment makeCallFragment;
    private boolean flag = false;
    private int REQUEST_CODE = 256;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Intent mServiceIntent;

    private ImageView alertBtn;

    public Location location;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager mLocationManager;

    //private Geocoder geocoder = new Geocoder();

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("PROGRESS REPORT",String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                //drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.e("ERROR","Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setFragment();

        alertBtn = findViewById(R.id.alert);

        //alertBtn.setOnClickListener(new View.OnClickListener() {
            //@Override
          //  public void onClick(View v) {

        if (!isMyServiceRunning(ShakeService.class)) {
               // mServiceIntent = new Intent(MainActivity.this, ServiceActivity.class);
                //startActivity(mServiceIntent);
            Intent intent = new Intent(this, ShakeService.class);
            //Start Service
            startService(intent);

        }
      //  });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                location = getCurrentLocation();
                APIService service = RetrofitClient.getClient(BASE_URL).create(APIService.class);

                if(location == null){
                    Toast.makeText(getApplicationContext(), "Could not get Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<Void> call = service.sendAlert("DISTRESS",
                        location.getLatitude(), location.getLongitude());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if(response.isSuccessful()){
                            //Toast.makeText(getApplicationContext(),response.body().toString(),
                            //Toast.LENGTH_SHORT);
                            //Log.i("LOG!!!!", "post submitted to API." + response.body().toString());
                            Toast.makeText(getApplicationContext(),"Upload successfull", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Log.e("ERROR-T", "Unable to submit post to API");
                        Toast.makeText(getApplicationContext(),"UNABLE TO SUBMIT POST TO API" + t.getMessage(), Toast.LENGTH_LONG).show();


                    }
                });
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
               // tvShake.setText("Shake Action is just detected!!");
                Toast.makeText(MainActivity.this, "Shaked!!!", Toast.LENGTH_LONG).show();
                Log.i("SUCESS","SHAKE IS STILL WORKING");
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //getCurrentLocation();

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        //mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        super.onDestroy();

    }

    public void onClickProfile(View view){

        Intent intent = new Intent(this,ChildProfileActivity.class);
        startActivity(intent);


    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE){
//
//        }
//    }

    public  Location getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            //Snackbar.make(mMapView, R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
            Toast.makeText(this,"ERROR", Toast.LENGTH_LONG);
        }
        else {
            if (isNetworkEnabled) {
                try {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(location.getLatitude(),
                            location.getLongitude(),getApplicationContext(),new GeocoderHandler());
                    return location;
                }
                catch(SecurityException e){
                    e.printStackTrace();
                }
            }

            if (isGPSEnabled) {
                try {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);

                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    return location;
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        }
        if (location != null) {
            Log.d("PROGRESS REPORT",String.format("getCurrentLocation(%f, %f)", location.getLatitude(),
                    location.getLongitude()));
           // drawMarker(location);
        }

        return location;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //tvAddress.setText(locationAddress);
            //Toast.makeText(getApplicationContext(),locationAddress,Toast.LENGTH_LONG).show();
        }
    }

    public void onCLickAlert(View view){

       location = getCurrentLocation();
        APIService service = RetrofitClient.getClient(BASE_URL).create(APIService.class);

        if(location == null){
            Toast.makeText(this, "Could not get Location", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = service.sendAlert("DISTRESS",
                location.getLatitude(), location.getLongitude());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){
                    //Toast.makeText(getApplicationContext(),response.body().toString(),
                            //Toast.LENGTH_SHORT);
                    //Log.i("LOG!!!!", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(),"Upload successfull", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.e("ERROR-T", "Unable to submit post to API");
                Toast.makeText(getApplicationContext(),"UNABLE TO SUBMIT POST TO API" + t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });



    }

    public void onClickCall(View view){
        Intent intent = new Intent(this,MakeCallActivity.class);
        startActivity(intent);
    }

}

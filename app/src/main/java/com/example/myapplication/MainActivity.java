package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.location.Locationrequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.location.LocationManager.GPS_PROVIDER;

import android.location.LocationListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    public LocationManager locationManager;
    public TextView latlon, gf;
    public int lng, lti;
    protected Context context;
    private final int REQUEST_LOCATION_PERMISSION = 1, xx=0;
    private GoogleMap mMap;
    private int REQUEST_CHECK_SETTINGS = 1;
    public static int ff=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latlon = (TextView) findViewById(R.id.latlon);
        gf = (TextView) findViewById(R.id.gf);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        openGPSSettings();






        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Internet not granted", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this, "Permissions not granted", Toast.LENGTH_LONG).show();
            latlon.setText("Location coordinates couldn't be loaded due to Permissions inaccess.");
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, );
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    xx);
        }


        //Criteria is being used okayishly(Without errors) without much effect on the accuracy
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        locationManager.requestLocationUpdates(100, 0, criteria, this, null);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createlocationRequest());
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        final Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
//                    while (ff!=1){
                        try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().

                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
//                            task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
//                                @Override
//                                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                                    ff=1;
//                                }
//                            });

                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
//                }
            }
        });


    }

    public void Loc(View view){
        Intent intent = new Intent(MainActivity.this, Loc.class);
        startActivity(intent);
    }

    public void Locc(View view){
        Intent intent = new Intent(MainActivity.this, Loc.class);
        startActivity(intent);
    }

    protected LocationRequest createlocationRequest(){
        return LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(1000);
    }



    @Override
    public void onLocationChanged(Location location) {
         double x = location.getLatitude();
        double y = location.getLongitude();
        if(x>12.9247 && x<12.9395 && y>77.608 && y<77.6424)
            gf.setText("The Delivery person has reached Koramangla.");
        else
            gf.setText("The delivery person is yet to reach Koramangla.");
        sydney = new LatLng(x,y);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        latlon.setText("Latitude: " + location.getLatitude() + ", \nLongitude: " + location.getLongitude() +
                " \nAccuracy: " + location.getAccuracy());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS ", Toast.LENGTH_SHORT).show();
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    public void click(View view){
        Intent intt = new Intent(MainActivity.this, Loc.class);
        startActivity(intt);
    }

    public LatLng sydney = new LatLng(-24, 15);

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.



        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}

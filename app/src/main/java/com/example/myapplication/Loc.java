package com.example.myapplication;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;



import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.graphics.Color.argb;

public class Loc extends AppCompatActivity implements android.location.LocationListener, OnMapReadyCallback {
        public LocationManager locationManager;
        public TextView latlon, gf;
        public int lng, lti;
        protected Context context;
        private final int REQUEST_LOCATION_PERMISSION = 1, xx = 0;
        private GoogleMap mMap;
        private static final long duration = 60*60*1000;
        private static final String id = "Outlet1";
        private static final float radius = 500.0f;
        private static String TAG = "DEBUGGGGGG";
        private GeofencingClient geofencingclient;
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


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(com.example.myapplication.Loc.this, "Internet not granted", Toast.LENGTH_LONG).show();
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
                Toast.makeText(com.example.myapplication.Loc.this, "Permissions not granted", Toast.LENGTH_LONG).show();
                latlon.setText("Location coordinates couldn't be loaded due to Permissions inaccess.");
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, );
                ActivityCompat.requestPermissions(com.example.myapplication.Loc.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                ActivityCompat.requestPermissions(com.example.myapplication.Loc.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        xx);
            }


//            Criteria is being used okayishly(Without errors) without much effect on the accuracy
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        locationManager.requestLocationUpdates(100, 0, criteria, this, null);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);

//            showNotification("njf", "ashdjk");
            Toast.makeText(this, "shown", Toast.LENGTH_LONG).show();

        }

    public void showNotification(String text, String bigtext){
//        NotificationManager nf = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, Loc.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingnotifs = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(text)
//                .setContentText(text)
//                .setContentIntent(pendingnotifs)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigtext))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .build();
//        nf.notify(0, notification);

        int nid = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("A notification")
                .setContentText("You have started the task app")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId =  "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readabale titel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(nid, builder.build());


    }
        private Geofence createGeofence(LatLng latlng, float radius){
            Log.d(TAG, "Geofence create");
            return new Geofence.Builder()
                    .setRequestId(id)
                    .setCircularRegion(latlng.latitude, latlng.longitude, radius)
                    .setExpirationDuration(duration)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
        }
        private static int f=0;
        private GeofencingRequest gefencingrequest(Geofence geofence){
            Log.d(TAG, "create Geofence request");
            return new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build();
        }

        private PendingIntent pendingintent;
        private final int GEOFENCE_REQ_CODE = 0;
        private PendingIntent createGeofencePendingIntent() {
            Log.d(TAG, "PendingIntent");
            if ( pendingintent != null )
                return pendingintent;

            Intent intent = new Intent( this, GeofenceTransitionsIntentService.class);
            return PendingIntent.getService(
                    this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        }

        private Circle geoFenceLimits;
        private void drawGeofence(LatLng latlng, int radius) {
            Log.d(TAG, "drawGeofence()");



        }



        @Override
        public void onLocationChanged(Location location) {
            double x = location.getLatitude();
            double y = location.getLongitude();
            LatLng pos = new LatLng(12.9316982,77.6243903);
//            CircleOptions circleOptions = new CircleOptions()
//                    .center(pos)
//                    .strokeColor(argb(50, 70,70,70))
//                    .fillColor( argb(100, 150,150,150) )
//                    .radius( radius )
//                    .visible(true);
//
//            mMap.addCircle( circleOptions );
            double a = Math.abs(x-pos.latitude);
            double b = Math.abs(y-pos.longitude);
            double c = Math.pow(a,2) + Math.pow(b, 2);
            double amax = 0.0083, bmax = 0.0162;
            double cmax = Math.pow(amax, 2) + Math.pow(bmax, 2);
            if(c < cmax){
                gf.setText("The Delivery person has reached Koramangla.");
            }

//            if (x > 12.9247 && x < 12.9395 && y > 77.608 && y < 77.6424)
//                gf.setText("The Delivery person has reached Koramangla.");
            else
                gf.setText("The delivery person is yet to reach Koramangla.");
            sydney = new LatLng(x, y);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
            if(f==0) {
                mMap.addPolyline(new PolylineOptions().add(pos).add(sydney).width(5f).
                        color(argb(50, 27, 27, 208)));
                mMap.addCircle(new CircleOptions().center(pos).radius(2000.0).strokeWidth(5f)
                        .strokeColor(argb(50, 27, 27, 208)).fillColor(argb(50, 27, 27, 27))
                );
                f=1;
            }
            LatLng laatlng = new LatLng(12.9316982,77.6243903);

            gefencingrequest(createGeofence(laatlng, 5000));
            createGeofencePendingIntent();
            drawGeofence(laatlng, 5000);
                latlon.setText("Latitude: " + location.getLatitude() + ", \nLongitude: " + location.getLongitude() +
                    " \nAccuracy: " + location.getAccuracy());



        }


        geofencingclient.add

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(Loc.this, "Please Enable GPS ", Toast.LENGTH_SHORT).show();
            Log.d("Latitude", "enable");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Latitude", "enable");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Latitude", "enable");
        }


        public LatLng sydney = new LatLng(-24, 15);

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney, Australia, and move the camera.


            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }
    }

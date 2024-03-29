package com.example.myapplication;


import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class GPSprovider extends Service implements LocationListener{



    public LocationManager locationManager;
    private String gf;
    private LatLng sydney;
    private  long startTime, endTime;

    private database dbase;


    @Override
    public void onCreate() {
        Log.d("AAA", "oncreate");
        startTime = System.currentTimeMillis();
        endTime = startTime + 60000;
        dbase = new database(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onlocationchanged", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return super.onStartCommand(intent, flags, startId);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
//        return super.onStartCommand(intent, flags, startId);

        return Service.START_STICKY;
    }




    public void onLocationChanged(Location location) {
        Log.d("AAA", "onlocationchanged");
        Toast.makeText(this, "onlocationchanged", Toast.LENGTH_SHORT).show();
        double x = location.getLatitude();
        double y = location.getLongitude();
        if(x>12.9247 && x<12.9395 && y>77.608 && y<77.6424)
            gf = "The Delivery person has reached Koramangla.";
        else
            gf = ("The delivery person is yet to reach Koramangla.");
        sydney = new LatLng(x,y);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(x + "")
                .setContentText(y + "")
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .build();
        startForeground(1337, notification);
        sendDataToActivity(x, y);
        AddData(x, y);
//        if(System.currentTimeMillis()>endTime)
//            onDestroy();

    }

    public void sendDataToActivity(double x, double y){
        Intent intent = new Intent("Location Updates");
        intent.putExtra("Longitude", x + "");
        intent.putExtra("Latitude", y+"");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Log.d("AAA", "Data Sent " + x);
        showNotification(x+"", y+"");
    }

    public void AddData(double x, double y){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timee = sdf.format(date);
        boolean isInserted = dbase.insertData(timee, x + "", y+"");
        if(isInserted == true)
            Log.d("AAA", "Data Added to SQLite");
        else
            Log.d("AAA", "Data Couldn't be Added to SQLite");
    }





    @Override
    public void onDestroy() {
        Log.d("AAA","ondestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS ", Toast.LENGTH_SHORT).show();
        Log.d("Latitude","enable");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","enable");
    }


    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    public void showNotification(String text, String bigtext){
        int nid = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(text)
                .setContentText(bigtext)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId =  "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(nid, builder.build());



    }
}

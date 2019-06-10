package com.example.myapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.ContentValues.TAG;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String Tag = "Geofence Transitions";

    public GeofenceTransitionsIntentService() {
        super("Intent Service");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SERVICe", "oncreate()");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d("SERVICe", "startcommand");


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SERVICe", "onbind()");
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("SERVICe", "onunbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d("SERVICe", "onstart()");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICe", "oncdestroy()");
    }


    @Nullable

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            Log.e(TAG,  "Geofencing Error" + geofencingEvent.getErrorCode());
            return;
        }
        String x = intent.getDataString();
        Log.d("SERVICe", "intentt()");

        Toast.makeText(this, "shown222", Toast.LENGTH_LONG).show();
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
         Log.d("SERVICe", Integer.toString(geofenceTransition));
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            showNotification("Entered", "Entered the location");
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            showNotification("Exited", "Exited from the location");
        else
            showNotification("Error", "Error");


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

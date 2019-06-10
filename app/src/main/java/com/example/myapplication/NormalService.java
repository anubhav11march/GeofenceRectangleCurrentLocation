package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

public class NormalService extends Service {
    public NormalService(){
        super();
    }


        int mStartMode;       // indicates how to behave if the service is killed
        IBinder mBinder;      // interface for clients that bind
        boolean mAllowRebind;
        String data;// indicates whether onRebind should be used

        @Override
        public void onCreate() {
            Log.d("SErvice", "onCreate()");
            Toast.makeText(this, "sdgf", Toast.LENGTH_SHORT).show();

            // The service is being created
        }



    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // The service is starting, due to a call to startService()

            Log.d("SErvice", "startcommand()");
            Bundle bundle = intent.getExtras();
            data = (String) bundle.getString("Name");
        showNotification("xxx", data);

            return mStartMode;
        }


        @Override
        public IBinder onBind(Intent intent) {
            // A client is binding to the service with bindService()
            Log.d("SErvice", "onbind()");
            return mBinder;
        }
        @Override
        public boolean onUnbind(Intent intent) {
            // All clients have unbound with unbindService()
            Log.d("SErvice", "unbind()");
            return mAllowRebind;
        }
        @Override
        public void onRebind(Intent intent) {
            // A client is binding to the service with bindService(),
            Log.d("SErvice", "rebind()");
            // after onUnbind() has already been called
        }
        @Override
        public void onDestroy() {
            Log.d("SErvice", "destroy()");
            // The service is no longer used and is being destroyed
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



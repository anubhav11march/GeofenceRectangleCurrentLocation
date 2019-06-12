package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class BoundService extends Service {

    public BoundService(){

    }

    private final IBinder localBinder = new MyBinder();

    @Override
    public void onCreate() {
        Log.d("AAAA", "oncreate");
        super.onCreate();
    }

    public class MyBinder extends Binder{
        public BoundService getService(){
            Log.d("AAAA", "Mybinder");
            return BoundService.this;
        }
    }
    public int randomGenerator(){
        Random rand = new Random();
        int number = rand.nextInt();
        Log.d("AAAA", "randomgenerator");
        return number;
    }

    @Override
    public void onDestroy() {
        Log.d("AAAA", "ondestroy");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("AAAA", "rebind");
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("AAAA", "onunbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("AAAA", "task removed");
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("AAAA", "onbind");
        return localBinder;
    }
}

package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
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
        Log.d("AAAA", "oncreateee");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AAAA", "startcommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("AAAA", "onbind");
        return localBinder;
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



        public void SuccessListener( ){
            this.listenerr  = null;
            getResponse();
        }

        public void setListenerr(onSuccessListenerr listenerr){
            this.listenerr = listenerr;
            getResponse();
        }
        private  onSuccessListenerr listenerr = null;

        public interface onSuccessListenerr{
            public void onSuccess(int x);
            public void onFailure(String error);

        }

        int y=0;
        public onSuccessListenerr getListenerr() {
            return listenerr;
        }
        public void getResponse(){

//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//
//
//                        listenerr.onSuccess(randomGenerator());
//                    }
//                    catch (Exception e){
//                        listenerr.onFailure(String.valueOf(e));
//                    }
//
//                }
//            };

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {


                        listenerr.onSuccess(randomGenerator());
                    }
                    catch (Exception e){
                        listenerr.onFailure(String.valueOf(e));
                    }

                }
            }, 3000);


        }

    }





//package com.example.myapplication;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.util.Log;
//
//public class SuccessListener {
//
//    private BoundService boundService;
//    private boolean isBound = false;
//    public SuccessListener( int x){
//        this.listenerr  = null;
//        getResponse(x);
//    }
//
//    public void setListenerr(onSuccessListenerr listenerr){
//        this.listenerr = listenerr;
//    }
//    private onSuccessListenerr listenerr;
//
//    public interface onSuccessListenerr{
//        public void onSuccess(int x);
//        public void onFailure(String error);
//
//    }
//
//    int y=0;
//    public onSuccessListenerr getListenerr() {
//        return listenerr;
//    }
//    public void getResponse(final int x){
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//
//                    listenerr.onSuccess(x);
//                }
//                catch (Exception e){
//                    listenerr.onFailure(String.valueOf(e));
//                }
//
//            }
//        };
//
//        Handler handler = new Handler();
//        handler.postDelayed(runnable,2000);
//
//
//    }
//
//}

package com.example.myapplication;

import android.os.Handler;
import android.util.Log;

public class SuccessListener {

    private BoundService boundService;
    private boolean isBound = false;
    public SuccessListener(){
        this.listenerr  = null;
        getResponse();
    }

    public void setListenerr(onSuccessListenerr listenerr){
        this.listenerr = listenerr;
    }
    private onSuccessListenerr listenerr;

    public interface onSuccessListenerr{
        public void onSuccess();
        public void onFailure();

    }

    public onSuccessListenerr getListenerr() {
        return listenerr;
    }
    public void getResponse(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int x=boundService.randomGenerator();
                    listenerr.onSuccess();
                }
                catch (Exception e){
                    listenerr.onFailure();
                }

            }
        };


    }

}

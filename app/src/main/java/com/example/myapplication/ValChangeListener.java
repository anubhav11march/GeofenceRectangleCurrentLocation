package com.example.myapplication;

import android.os.Handler;

public class ValChangeListener {
    public void ValChangeListener(){
        this.listener = null;
        getResponse();
    }

    private onValChangeListener listener;

    public void setListener(onValChangeListener listener){
        this.listener = listener;
    }

    public interface onValChangeListener{
        public void onSuccess();
        public void onFailure();

    }

    public onValChangeListener getListener() {
        return listener;
    }

    public void getResponse(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {


                    listener.onSuccess();
                }
                catch (Exception e){
                    listener.onFailure();
                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,2000);

    }

}

package com.example.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.location.LocationManager.GPS_PROVIDER;

import android.location.LocationListener;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    public LocationManager locationManager;
    public TextView latlon, gf, vv;
    public int lng, lti;
    protected Context context;
    private final int REQUEST_LOCATION_PERMISSION = 1, xx = 0;
    private GoogleMap mMap;
    private int REQUEST_CHECK_SETTINGS = 1, f = 0;
    private TextView x;
    private String xxx;
    private String yyy;
    private TextView db;
    private database dbase;
    private BoundService boundService;
    private boolean isBound = false;
    private GPSprovider gpss = new GPSprovider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latlon = (TextView) findViewById(R.id.latlon);
        vv = (TextView) findViewById(R.id.latlon2);
        db = (TextView) findViewById(R.id.ll);
        dbase = new database(this);
        gf = (TextView) findViewById(R.id.gf);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



//        openGPSSettings();
        x = (TextView) findViewById(R.id.gff);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
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


//        Criteria is being used okay
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        locationManager.requestLocationUpdates(100, 0, criteria, this, null);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createlocationRequest());
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });



        {
            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException sendEx) {

                        }
                    }
                }
            });

        }

        Intent intentt = new Intent(this, BoundService.class);
startService(intentt);

            bindService(intentt,  boundServiceConnection, 0);


//        Log.d("AAAAA","Component name " + name.getClassName());
//        Log.d("AAAAA"," Bind Result = " + result);


        startService(new Intent(MainActivity.this, GPSprovider.class));

    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.AppTask> tasks = manager.getAppTasks();
//        for (ActivityManager.AppTask task : tasks) {
//            if (serviceClass.getName().equals(task.getTaskInfo().baseActivity))
//                return true;
//        }
//        return false;
//    }

//
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
    public void getData(){
        Cursor res = dbase.getData();
        if(res.getCount() == 0)
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
        StringBuffer sb = new StringBuffer();
//        int i=0;
//        while (res.moveToNext()){
//            i++;
//        }
//        res.moveToPrevious();
        res.moveToLast();
        sb.append(res.getString(0) + "\n");
        sb.append(res.getString(2) + "\n");
        sb.append(res.getString(1) + "\n");
        db.setText(sb.toString());
        res.close();
    }

    private BroadcastReceiver messageservice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            xxx = intent.getStringExtra("Longitude");
            yyy = intent.getStringExtra("Latitude");
            Log.d("AAA", "Received data " + xxx);
            vv.setText(xxx + ", " + yyy);

            getData();
        }
    };

    @Override
    protected void onResume() {
        Log.d("AAAAA","Listener:");



        //Bounded Service

//


            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if(isBound){
                    Toast.makeText(MainActivity.this, String.valueOf(boundService.randomGenerator()), Toast.LENGTH_SHORT).show();
                    Log.d("AAAA", "Generated Number: " + boundService.randomGenerator());


                        boundService.setListenerr(new BoundService.onSuccessListenerr() {
                            @Override
                            public void onSuccess(int x) {
                                Log.d("AAAAAA", "Successsssss");
                            }

                            @Override
                            public void onFailure(String error) {
                                Log.d("AAAAAA", "FAIL");
                            }
                        });

                }
                }
            };

                Handler handler = new Handler();
                handler.postDelayed(runnable, 1000);
//            handler.post(runnable);

        //Normal service
        LocalBroadcastManager.getInstance(this).registerReceiver(messageservice, new IntentFilter("Location Updates"));



        Log.d("AAA", xxx + " Received");


        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createlocationRequest());
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });
        /*for (int i=0; i<5; i++) {
            Handler handler = new Handler();
            final int finalI = i;
            handler.post(new Runnable() {
                @Override
                public void run() {

                    x.setText("" + finalI);
                }
            });
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        new Thread(new Runnable() {
            @Override
            public void run() {

                Handler handler = new Handler(getMainLooper());
                for (int i = 0 ;i<5 ;i++) {
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            x.setText(finalI +"");
                        }
                    },5000);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }





                /*Looper.prepare();
                Handler handler = new Handler(getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i<5 ; i++) {
                            x.setText(i+"");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },1000);
                Looper.loop();*/
            }
        }).start();

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        Log.d("YOOO", "STARTED");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

//            startService(new Intent(MainActivity.this, NormalService.class).putExtra("Name", "NAMEE"));

//        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        Log.d("YOOO", "STARTED");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void unbindButton(View view){
        unbindService(boundServiceConnection);
    }



    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                messageservice);
        super.onStop();
        if(isBound){
            boundService.setListenerr(null);
            unbindService(boundServiceConnection);

            //isBound = false;
//            Toast.makeText(context, isBound + "", Toast.LENGTH_SHORT).show();
            Log.d("AAAA", isBound + "");

        }

        locationManager.removeUpdates(this);
        locationManager.removeUpdates(this);
        Log.d("YOOO", "REMOVED");

    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyBinder binderBridge = (BoundService.MyBinder) iBinder;
            boundService = binderBridge.getService();
            isBound = true;
            Log.d("AAAA", "Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            boundService = null;
            Log.d("AAAA", "Not Connected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("AAAA", isBound + "");

    }

    public void Loc(View view){
        Intent intent = new Intent(MainActivity.this, Loc.class);
        startActivity(intent);
    }

    public void Locc(View view){
        Intent intent = new Intent(MainActivity.this, SQLite.class);
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
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location").snippet("Your current location")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

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

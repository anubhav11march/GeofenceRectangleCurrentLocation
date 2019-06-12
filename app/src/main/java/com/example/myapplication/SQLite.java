package com.example.myapplication;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLite extends AppCompatActivity {
    private TextView db;
    private database dbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        db = (TextView) findViewById(R.id.ll);
        dbase = new database(this);



    }

    public void AddData(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timee = sdf.format(date);
        boolean isInserted = dbase.insertData(timee, 12.9109567 + "", 77.6520983+"");
        if(isInserted == true)
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "There's some problem.", Toast.LENGTH_SHORT).show();
    }


    public void getData(View view){
        Cursor res = dbase.getData();
        if(res.getCount() == 0)
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
        StringBuffer sb = new StringBuffer();
        while (res.moveToNext()){
            sb.append(res.getString(0) + "\n");
            sb.append(res.getString(1) + "\n");
            sb.append(res.getString(2) + "\n");

        }
        db.setText(sb.toString());
    }
}

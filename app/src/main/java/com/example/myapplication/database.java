package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.myapplication.SQLite;

import java.net.IDN;

public class database extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Location.db";
    public final static String TABLE_NAME = "location_data";
    public final static String time = "TIME";
    public final static String Lng = "LNG";
    public final static String Ltd = "LTD";

    public database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + "(TIME TEXT,LNG TEXT,LTD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String TIME, String LTD, String LNG){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(time, TIME);
        cv.put(Lng, LNG);
        cv.put(Ltd, LTD);
        long res = db.insert(TABLE_NAME, null, cv);
        //if (db)
        if(res == -1) {

            return false;
        }
        else return true;
    }

    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res;

    }
}

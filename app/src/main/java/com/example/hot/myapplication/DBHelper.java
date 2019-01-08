package com.example.hot.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "YourNote";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_NOTES = "notes";

    public static final String KEY_ID = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE = "note";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_CONTACTS + "("+ KEY_ID + " integer primary key," + KEY_LOGIN + " text unique," + KEY_NAME + " text," + KEY_PASSWORD + " text" + ")");

        db.execSQL("create table " + TABLE_NOTES + "("+ KEY_ID + " integer primary key," + KEY_LOGIN + " text," + KEY_DATE + " date," + KEY_NOTE + " text," + " foreign key ("+KEY_LOGIN+") REFERENCES "+TABLE_CONTACTS+"("+KEY_LOGIN+"));");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
    public boolean updateData(String data, String login, String note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NOTE,note);

        int result = db.update(TABLE_NOTES,cv,"login=? and date=? ",new String[]{login,data});

        if(result>0)
        {
            return true;
        }else
            return false;
    }
}
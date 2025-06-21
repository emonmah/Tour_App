package com.example.tourapp;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "TourBooking.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE packages(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, location TEXT, price REAL, organizer_id INTEGER, status TEXT)");
        db.execSQL("CREATE TABLE bookings(id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, package_id INTEGER, status TEXT, payment_status TEXT)");
        db.execSQL("CREATE TABLE reviews(id INTEGER PRIMARY KEY AUTOINCREMENT, package_id INTEGER, customer_id INTEGER, rating INTEGER, review TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS packages");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        db.execSQL("DROP TABLE IF EXISTS reviews");
        onCreate(db);
    }
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

}


package com.example.tourapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PackageDetailsActivity extends AppCompatActivity {

    TextView titleTv, descTv, locationTv, priceTv;
    Button bookBtn;
    int packageId, customerId;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        titleTv = findViewById(R.id.titleTv);
        descTv = findViewById(R.id.descTv);
        locationTv = findViewById(R.id.locationTv);
        priceTv = findViewById(R.id.priceTv);
        bookBtn = findViewById(R.id.bookBtn);

        packageId = getIntent().getIntExtra("packageId", -1);
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        customerId = prefs.getInt("userId", -1);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        loadPackageDetails();

        bookBtn.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("customer_id", customerId);
            values.put("package_id", packageId);
            values.put("status", "booked"); // initially marked as booked

            db.insert("bookings", null, values);
            Toast.makeText(this, "Booking Confirmed", Toast.LENGTH_SHORT).show();
            bookBtn.setEnabled(false);
        });
    }

    void loadPackageDetails() {
        Cursor cursor = db.rawQuery("SELECT * FROM packages WHERE id = ?", new String[]{String.valueOf(packageId)});
        if (cursor.moveToFirst()) {
            titleTv.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            descTv.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            locationTv.setText(cursor.getString(cursor.getColumnIndexOrThrow("location")));
            priceTv.setText("Price: $" + cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
        }
        cursor.close();
    }
}

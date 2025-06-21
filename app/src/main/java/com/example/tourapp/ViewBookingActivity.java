package com.example.tourapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewBookingActivity extends AppCompatActivity {
    ListView bookingListView;
    ArrayList<String> bookingList;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        organizerId = prefs.getInt("userId", -1);

        bookingListView = findViewById(R.id.bookingListView);
        bookingList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingList);
        bookingListView.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        loadBookings();
    }

    void loadBookings() {
        bookingList.clear();
        String query =
                "SELECT b.id, b.status, b.payment_status, u.name AS customer_name, p.title " +
                        "FROM bookings b " +
                        "JOIN packages p ON b.package_id = p.id " +
                        "JOIN users u ON b.customer_id = u.id " +
                        "WHERE p.organizer_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(organizerId)});

        while (cursor.moveToNext()) {
            String customer = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("payment_status"));
            bookingList.add("Package: " + title + "\nCustomer: " + customer + "\nPayment: " + paymentStatus);
        }
        adapter.notifyDataSetChanged();
    }
}
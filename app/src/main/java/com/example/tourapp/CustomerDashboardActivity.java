package com.example.tourapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerDashboardActivity extends AppCompatActivity {

    ListView packageListView;
    ArrayList<String> packageList;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        customerId = prefs.getInt("userId", -1);

        packageListView = findViewById(R.id.packageListView);
        packageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, packageList);
        packageListView.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        loadPackages();

        packageListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, PackageDetailsActivity.class);
            Intent packageId = i.putExtra("packageId", position + 1);// Use real ID in practice
            startActivity(i);
        });

    }

    void loadPackages() {
        Cursor cursor = db.rawQuery("SELECT * FROM packages WHERE status = ?", new String[]{"approved"});
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            packageList.add(title + "\n" + loc + " - $" + price);
        }
        adapter.notifyDataSetChanged();
    }
}

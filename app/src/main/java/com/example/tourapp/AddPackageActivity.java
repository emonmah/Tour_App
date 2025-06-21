package com.example.tourapp;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddPackageActivity extends AppCompatActivity {

    EditText titleEt, descEt, locationEt, priceEt;
    Button saveBtn;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        titleEt = findViewById(R.id.titleEt);
        descEt = findViewById(R.id.descEt);
        locationEt = findViewById(R.id.locationEt);
        priceEt = findViewById(R.id.priceEt);
        saveBtn = findViewById(R.id.saveBtn);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        organizerId = prefs.getInt("userId", -1);

        saveBtn.setOnClickListener(v -> {
            String title = titleEt.getText().toString();
            String desc = descEt.getText().toString();
            String location = locationEt.getText().toString();
            String priceStr = priceEt.getText().toString();

            if (title.isEmpty() || desc.isEmpty() || location.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", desc);
            values.put("location", location);
            values.put("price", price);
            values.put("status", "pending");
            values.put("organizer_id", organizerId);

            db.insert("packages", null, values);
            Toast.makeText(this, "Package submitted for approval", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

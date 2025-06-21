package com.example.tourapp;


import android.content.ContentValues;
import android.database.Cursor;
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

public class EditPackageActivity extends AppCompatActivity {

    EditText titleEt, descEt, locationEt, priceEt;
    Button updateBtn;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int packageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_package);

        titleEt = findViewById(R.id.titleEt);
        descEt = findViewById(R.id.descEt);
        locationEt = findViewById(R.id.locationEt);
        priceEt = findViewById(R.id.priceEt);
        updateBtn = findViewById(R.id.updateBtn);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        packageId = getIntent().getIntExtra("packageId", -1);

        loadPackageDetails();

        updateBtn.setOnClickListener(v -> {
            String title = titleEt.getText().toString();
            String desc = descEt.getText().toString();
            String location = locationEt.getText().toString();
            String priceStr = priceEt.getText().toString();

            if (title.isEmpty() || desc.isEmpty() || location.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", desc);
            values.put("location", location);
            values.put("price", price);
            values.put("status", "pending"); // reset approval

            db.update("packages", values, "id=?", new String[]{String.valueOf(packageId)});
            Toast.makeText(this, "Package updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    void loadPackageDetails() {
        Cursor cursor = db.rawQuery("SELECT * FROM packages WHERE id = ?", new String[]{String.valueOf(packageId)});
        if (cursor.moveToFirst()) {
            titleEt.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            descEt.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            locationEt.setText(cursor.getString(cursor.getColumnIndexOrThrow("location")));
            priceEt.setText(cursor.getString(cursor.getColumnIndexOrThrow("price")));
        }
        cursor.close();
    }
}

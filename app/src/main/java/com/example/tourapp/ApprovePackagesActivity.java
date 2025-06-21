package com.example.tourapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ApprovePackagesActivity extends AppCompatActivity {

    ListView packageListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> pendingList;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_packages);

        packageListView = findViewById(R.id.packageListView);
        pendingList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pendingList);
        packageListView.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        loadPendingPackages();

        packageListView.setOnItemClickListener((parent, view, position, id) -> {
            int actualId = getPackageIdFromPosition(position);
            showApproveRejectDialog(actualId);
        });
    }

    void loadPendingPackages() {
        pendingList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM packages WHERE status = 'pending'", null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            pendingList.add("Title: " + title + "\nLocation: " + location + "\nPrice: $" + price);
        }
        adapter.notifyDataSetChanged();
    }

    int getPackageIdFromPosition(int position) {
        Cursor cursor = db.rawQuery("SELECT id FROM packages WHERE status = 'pending'", null);
        int id = -1;
        int i = 0;
        while (cursor.moveToNext()) {
            if (i == position) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                break;
            }
            i++;
        }
        cursor.close();
        return id;
    }

    void showApproveRejectDialog(int packageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Approve or Reject Package")
                .setItems(new String[]{"Approve", "Reject"}, (dialog, which) -> {
                    String newStatus = (which == 0) ? "approved" : "rejected";
                    ContentValues values = new ContentValues();
                    values.put("status", newStatus);
                    db.update("packages", values, "id=?", new String[]{String.valueOf(packageId)});
                    Toast.makeText(this, "Package " + newStatus, Toast.LENGTH_SHORT).show();
                    loadPendingPackages();
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPendingPackages();
    }
}
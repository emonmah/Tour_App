package com.example.tourapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ManagePackagesActivity extends AppCompatActivity {

    ListView packageListView;
    ArrayList<String> packageList;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_packages);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        organizerId = prefs.getInt("userId", -1);

        packageListView = findViewById(R.id.packageListView);
        packageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, packageList);
        packageListView.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        loadPackages();

        packageListView.setOnItemClickListener((parent, view, position, id) -> {
            int realId = position + 1; // in production, use actual ID
            showEditDeleteDialog(realId);
        });

        findViewById(R.id.addPackageBtn).setOnClickListener(v ->
                startActivity(new Intent(this, AddPackageActivity.class)));
    }

    void loadPackages() {
        packageList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM packages WHERE organizer_id=?", new String[]{String.valueOf(organizerId)});
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            packageList.add(title + "\n" + loc + " - $" + price + " [" + status + "]");
        }
        adapter.notifyDataSetChanged();
    }

    void showEditDeleteDialog(int packageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action")
                .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(this, EditPackageActivity.class);
                        intent.putExtra("packageId", packageId);
                        startActivity(intent);
                    } else {
                        db.delete("packages", "id=?", new String[]{String.valueOf(packageId)});
                        Toast.makeText(this, "Package deleted", Toast.LENGTH_SHORT).show();
                        loadPackages();
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPackages(); // refresh list after edit/add
    }
}
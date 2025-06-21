package com.example.tourapp;

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

public class ManageUsersActivity extends AppCompatActivity {

    ListView userListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> userList;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        userListView = findViewById(R.id.userListView);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        loadUsers();
    }

    void loadUsers() {
        userList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            userList.add(name + " - " + role + "\n" + email);
        }
        adapter.notifyDataSetChanged();
    }
}

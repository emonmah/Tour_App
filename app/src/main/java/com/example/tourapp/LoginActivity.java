package com.example.tourapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button loginBtn, Registrationbtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        Registrationbtn = findViewById(R.id.registrationBtn);

        Registrationbtn.setOnClickListener(view -> {
            startActivity(new Intent(this,RegisterActivity.class));
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                prefs.edit().putInt("userId", userId).putString("role", role).apply();

                if (role.equals("Customer")) {
                    startActivity(new Intent(this, CustomerDashboardActivity.class));
                } else if (role.equals("Organizer")) {
                    startActivity(new Intent(this, OrganizerDashboardActivity.class));
                } else if(role.equals("Admin")){
                    startActivity(new Intent(this, AdminDashboardActivity.class)); // Optional
                }
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

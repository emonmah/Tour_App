package com.example.tourapp;



import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEt, emailEt, passwordEt;
    Spinner roleSpinner;
    Button registerBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        nameEt = findViewById(R.id.edtname);
        emailEt = findViewById(R.id.edtmail);
        passwordEt = findViewById(R.id.edtpassword);
        roleSpinner = findViewById(R.id.spinner);
        registerBtn = findViewById(R.id.btnregister);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Customer", "Organizer"});
        roleSpinner.setAdapter(adapter);


        registerBtn.setOnClickListener(v -> {
            String name = nameEt.getText().toString();
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("email", email);
            cv.put("password", password);
            cv.put("role", role);
            db.insert("users", null, cv);
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}

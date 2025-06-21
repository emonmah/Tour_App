package com.example.tourapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboardActivity extends AppCompatActivity {

    Button manageUsersBtn, approvePackagesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        manageUsersBtn = findViewById(R.id.manageUsersBtn);
        approvePackagesBtn = findViewById(R.id.approvePackagesBtn);

        manageUsersBtn.setOnClickListener(v -> startActivity(new Intent(this, ManageUsersActivity.class)));
        approvePackagesBtn.setOnClickListener(v -> startActivity(new Intent(this, ApprovePackagesActivity.class)));
    }
}

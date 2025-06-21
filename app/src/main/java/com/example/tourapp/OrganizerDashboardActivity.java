package com.example.tourapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrganizerDashboardActivity extends AppCompatActivity {

    Button managePackagesBtn, viewBookingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        managePackagesBtn = findViewById(R.id.managePackagesBtn);
        viewBookingsBtn = findViewById(R.id.viewBookingsBtn);

        managePackagesBtn.setOnClickListener(v -> startActivity(new Intent(this, ManagePackagesActivity.class)));
        viewBookingsBtn.setOnClickListener(v -> startActivity(new Intent(this, ViewBookingActivity.class)));
    }
}

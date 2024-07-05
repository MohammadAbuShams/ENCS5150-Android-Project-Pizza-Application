package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public Toolbar toolbar;
    public static NavigationView navigationView;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize the database helper
        dataBaseHelper = new DataBaseHelper(this);

        // Fetch and display user data in the navigation header
        displayUserData(navigationView);

        // Add click listener for the menu logo
        ImageView imgMenuLogo = findViewById(R.id.imgMenuLogo);
        imgMenuLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        // Default fragment at launch
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ProfileAdminFragment()).commit();
        navigationView.setCheckedItem(R.id.navAdmin_profile);
    }

    private void displayUserData(NavigationView navigationView) {
        // Fetch user email from shared preferences
        String userEmail = SharedPrefManager.getInstance(this).readString("email", "");

        // Fetch user data from the database
        Cursor userCursor = dataBaseHelper.getUserByEmail(userEmail);
        if (userCursor != null && userCursor.moveToFirst()) {
            // Assign user data to navigation header
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.view_name);
            TextView navEmail = headerView.findViewById(R.id.view_email);

            @SuppressLint("Range") String firstName = userCursor.getString(userCursor.getColumnIndex("first_name"));
            @SuppressLint("Range") String lastName = userCursor.getString(userCursor.getColumnIndex("last_name"));
            @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex("email"));

            navUsername.setText(firstName + " " + lastName);
            navEmail.setText(email);
        }
        if (userCursor != null) {
            userCursor.close();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navAdmin_profile) {
            toolbar.setTitle("PROFILE");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ProfileAdminFragment()).commit();
        }
        if (item.getItemId() == R.id.navAdmin_addAdmin) {
            toolbar.setTitle("ADD ADMIN");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, SignUpFragment.newInstance("Admin"))
                    .commit();
        }

        if (item.getItemId() == R.id.navAdmin_viewAllOrders) {
            toolbar.setTitle("VIEW ALL ORDERS");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ViewAllOrdersFragment()).commit();
        }
        if (item.getItemId() == R.id.navAdmin_AddSpecialOffers) {
            toolbar.setTitle("ADD SPECIAL OFFERS");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new AddSpecialOfferFragment()).commit();
        }

        if (item.getItemId() == R.id.navAdmin_logout) {
            StringBuilder details = new StringBuilder();
            details.append("Are you sure you want to logout?");

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeAdminActivity.this);
            builder.setTitle("Confirm Logout")
                    .setMessage(details.toString())
                    .setNegativeButton("cancel", null)
                    .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(HomeAdminActivity.this, LogInActivity.class);
                            HomeAdminActivity.this.startActivity(intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

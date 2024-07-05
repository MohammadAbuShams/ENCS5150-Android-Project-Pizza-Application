package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class HomeNormalCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_normal_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize database helper
        dataBaseHelper = new DataBaseHelper(this);

        // Fetch and display user data in the navigation header
        displayUserData(navigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeCustomerFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        // Handle click event for the menu logo
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
    }

    private void displayUserData(NavigationView navigationView) {
        // Fetch user email from shared preferences
        String userEmail = SharedPrefManager.getInstance(this).readString("email", "");

        // Fetch user data from the database
        Cursor userCursor = dataBaseHelper.getUserByEmail(userEmail);
        if (userCursor != null && userCursor.moveToFirst()) {
            // Assign user data to navigation header
            View headerView = navigationView.getHeaderView(0);
            TextView textViewName = headerView.findViewById(R.id.view_name);
            TextView textViewEmail = headerView.findViewById(R.id.view_email);

            @SuppressLint("Range") String firstName = userCursor.getString(userCursor.getColumnIndex("first_name"));
            @SuppressLint("Range") String lastName = userCursor.getString(userCursor.getColumnIndex("last_name"));
            @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex("email"));

            textViewName.setText(firstName + " " + lastName);
            textViewEmail.setText(email);
        }
        if (userCursor != null) {
            userCursor.close();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeCustomerFragment()).commit();
        }

        if (item.getItemId() == R.id.nav_logout) {
            StringBuilder details = new StringBuilder();
            details.append("Are you sure you want to logout?");

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeNormalCustomerActivity.this);
            builder.setTitle("Confirm Logout")
                    .setMessage(details.toString())
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(HomeNormalCustomerActivity.this, LogInActivity.class);
                            HomeNormalCustomerActivity.this.startActivity(intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
        if (item.getItemId() == R.id.nav_PizzaMenu) {
            drawer.closeDrawer(GravityCompat.START);

            // Thread to not make the app freeze
            new Thread(() -> runOnUiThread(() -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PizzaMenuFragment()).commit();
            })).start();
        }

        if (item.getItemId() == R.id.nav_call_find_us) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CallOrFindUsFragment())
                    .commit();
        }

        if (item.getItemId() == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileCustomerFragment())
                    .commit();
        }

        if (item.getItemId() == R.id.nav_yourFavorites) {
            FavoritePizzaFragment favoriteFragment = FavoritePizzaFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.nav_yourOrders) {
            OrdersFragment ordersFragment = OrdersFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ordersFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.nav_specialOffers) {
            SpecialOffersFragment specialOffersFragment = SpecialOffersFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, specialOffersFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
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

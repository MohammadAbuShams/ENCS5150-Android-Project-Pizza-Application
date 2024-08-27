package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
/// importttt
public class MainActivity extends AppCompatActivity implements AsyncTaskCallback {
    private DataBaseHelper dataBaseHelper;
    private ProgressBar progressBar;
    private View screenOverlay;
    private String httpRequest = "https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/";
    private static final String DATABASE_NAME = "AdvancePizzaDB"; // Add your database name here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delete the previous database file

        // Initialize the database helper
        dataBaseHelper = new DataBaseHelper(this);

        Button getStarted = findViewById(R.id.button_GetStarted);
        progressBar = findViewById(R.id.barConnection);
        screenOverlay = findViewById(R.id.screenOverlay);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true); // Show the progress bar
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this, MainActivity.this);
                connectionAsyncTask.execute(httpRequest);
            }
        });
    }

    @Override
    public void onTaskComplete(boolean success) {
        showProgressBar(false); // Hide the progress bar regardless of result
        if (!success) {
            // Handle the error case
            Toast.makeText(MainActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the success case
            Toast.makeText(MainActivity.this, "Data fetched successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    public DataBaseHelper getDatabaseHelper() {
        return dataBaseHelper;
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        screenOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Method to delete the database file
    private void deleteDatabaseFile(Context context, String databaseName) {
        if (context.deleteDatabase(databaseName)) {
            Log.d("DatabaseOperation", "Database " + databaseName + " deleted.");
        } else {
            Log.d("DatabaseOperation", "Failed to delete database " + databaseName + ".");
        }
    }
}

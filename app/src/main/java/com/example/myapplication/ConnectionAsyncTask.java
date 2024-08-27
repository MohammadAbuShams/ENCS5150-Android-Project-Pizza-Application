package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, Void, String> {
    private WeakReference<Activity> weakActivity;
    private AsyncTaskCallback callback;
    private DataBaseHelper dataBaseHelper;

    public ConnectionAsyncTask(Activity activity, AsyncTaskCallback callback) {
        this.weakActivity = new WeakReference<>(activity);
        this.callback = callback;
        this.dataBaseHelper = new DataBaseHelper(activity); // Assuming DataBaseHelper is your database access class
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            // Simulate network call or replace with actual HTTP call
            return HttpManager.getData(params[0]);
        } catch (Exception e) {
            Log.e("HTTP", "Error fetching data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String json) {
        Activity activity = weakActivity.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (json == null) {
            Toast.makeText(activity, "Connection unsuccessful", Toast.LENGTH_LONG).show();
            callback.onTaskComplete(false);
        } else {
            Toast.makeText(activity, "Connection successful", Toast.LENGTH_LONG).show();
                    List<Pizza> pizzas = PizzaJsonParser.getObjectFromJson(json);
            if (pizzas == null || pizzas.isEmpty()) {
                Toast.makeText(activity, "No pizzas found or JSON error", Toast.LENGTH_LONG).show();
                callback.onTaskComplete(false);
                return;
            }

            // Insert or update pizzas in the database
            for (Pizza pizza : pizzas) {
                if (!dataBaseHelper.insertPizzas(pizza)) {
                    Toast.makeText(activity, "Failed to insert/update pizza: " + pizza.getName(), Toast.LENGTH_SHORT).show();
                }
            }
            callback.onTaskComplete(true);
        }
    }

    @Override
    protected void onPreExecute() {
        Activity activity = weakActivity.get();
        if (activity != null) {
            Toast.makeText(activity, "Starting connection...", Toast.LENGTH_SHORT).show();
        }
    }
}

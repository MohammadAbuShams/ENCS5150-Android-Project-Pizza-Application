package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FavoritePizzaFragment extends Fragment {

    private static final String TAG = "FavoritePizzaFragment";

    private RecyclerView recyclerView;
    private PizzaAdapter pizzaAdapter;
    private List<Pizza> favoritePizzas = new ArrayList<>();
    private ProgressBar progressBar;
    private DataBaseHelper dataBaseHelper;

    public static FavoritePizzaFragment newInstance() {
        return new FavoritePizzaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getContext()); // Initialize your database helper
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_pizza, container, false);
        setupRecyclerView(view);
        progressBar = view.findViewById(R.id.progressBar_favorite);
        loadFavoritePizzas();
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_favorite_pizza);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pizzaAdapter = new PizzaAdapter(getContext(), favoritePizzas);
        recyclerView.setAdapter(pizzaAdapter);
        Log.d(TAG, "RecyclerView setup complete");
    }

    private void loadFavoritePizzas() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Pizza> pizzas = new ArrayList<>();
            String userEmail = SharedPrefManager.getInstance(getContext()).readString("email", "");
            Log.d(TAG, "Loading favorites for user: " + userEmail);
            Cursor cursor = dataBaseHelper.getFavoritesWithPizzaInfoByEmail(userEmail);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    pizzas.add(createPizzaFromCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
                Log.d(TAG, "Loaded " + pizzas.size() + " favorite pizzas");
            } else {
                Log.d(TAG, "No favorite pizzas found or cursor is null");
            }

            getActivity().runOnUiThread(() -> {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    favoritePizzas.clear();
                    favoritePizzas.addAll(pizzas);
                    pizzaAdapter.notifyDataSetChanged();
                    Log.d(TAG, "RecyclerView adapter updated with favorite pizzas");
                }
            });
        }).start();
    }

    private Pizza createPizzaFromCursor(Cursor cursor) {
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.KEY_PIZZA_ID));
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_PIZZA_NAME));
        @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.KEY_PRICE));
        @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_SIZE));
        @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_CATEGORY));
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_DESCRIPTION));
        @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.KEY_IMAGE_ID));
        boolean isFavorite = true; // Since it's fetched from favorites
        Log.d(TAG, "Creating pizza from cursor: " + name);
        return new Pizza(id, name, price, size, category, description, imageId, isFavorite);
    }
}

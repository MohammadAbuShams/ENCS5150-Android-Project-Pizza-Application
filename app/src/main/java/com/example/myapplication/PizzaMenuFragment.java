package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PizzaMenuFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "PizzaMenuFragment";
    private RecyclerView recyclerView;
    private PizzaAdapter pizzaAdapter;
    private List<Pizza> pizzaList = new ArrayList<>();
    private List<Pizza> originalPizzaList = new ArrayList<>();  // Keep the original list for filtering
    private EditText searchField;
    private Spinner categorySpinner;
    private ImageButton filterButton;
    private DataBaseHelper dataBaseHelper;
    private ProgressBar progressBar;
    private SharedPrefManager sharedPrefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getContext());
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);
        initializeUI(view);
        return view;
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_pizza_menu);
        progressBar = view.findViewById(R.id.progressBar_pizzaMenu);
        searchField = view.findViewById(R.id.editText_search);
        categorySpinner = view.findViewById(R.id.spinner_categories);
        filterButton = view.findViewById(R.id.button_filter);

        setupRecyclerView();
        setupSearchField();
        setupCategorySpinner();
        setupFilterButton();
        loadPizzas();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pizzaAdapter = new PizzaAdapter(getContext(), pizzaList);
        recyclerView.setAdapter(pizzaAdapter);
    }

    private void setupSearchField() {
        searchField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPizzas(s.toString());
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);
    }

    private void setupFilterButton() {
        filterButton.setOnClickListener(v -> showFilterDialog());
    }

    private void loadPizzas() {
        String userEmail = sharedPrefManager.readString("email", null);
        if (userEmail != null) {
            Log.d(TAG, "Loading pizzas for user: " + userEmail);
        } else {
            Log.d(TAG, "Loading pizzas for user: No user logged in");
        }

        Cursor cursor = dataBaseHelper.getAllPizzas();
        if (cursor != null && cursor.moveToFirst()) {
            pizzaList.clear(); // Clear the list before adding new items
            originalPizzaList.clear(); // Clear the original list as well
            do {
                addPizzaFromCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(getContext(), "Failed to load pizzas from the database.", Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        pizzaAdapter.setPizzas(pizzaList);
    }

    private void addPizzaFromCursor(Cursor cursor) {
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.KEY_PIZZA_ID));
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_PIZZA_NAME));
        @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.KEY_PRICE));
        @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_SIZE));
        @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_CATEGORY));
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_DESCRIPTION));
        @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.KEY_IMAGE_ID));
        Pizza pizza = new Pizza(id, name, price, size, category, description, imageId, false);
        pizzaList.add(pizza);
        originalPizzaList.add(pizza);  // Keep the original list
    }

    private void filterPizzas(String query) {
        List<Pizza> filteredList = new ArrayList<>();
        for (Pizza pizza : originalPizzaList) {
            if (pizza.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(pizza);
            }
        }
        pizzaAdapter.setPizzas(filteredList);
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        Spinner priceSpinner = dialogView.findViewById(R.id.spinner_filter_price);
        Spinner sizeSpinner = dialogView.findViewById(R.id.spinner_filter_size);
        Spinner categorySpinner = dialogView.findViewById(R.id.spinner_filter_category);

        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.price_range, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            String selectedPrice = priceSpinner.getSelectedItem().toString();
            String selectedSize = sizeSpinner.getSelectedItem().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            applyFilters(selectedPrice, selectedSize, selectedCategory);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void applyFilters(String selectedPrice, String selectedSize, String selectedCategory) {
        Log.d(TAG, "Applying filters: Price=" + selectedPrice + ", Size=" + selectedSize + ", Category=" + selectedCategory);
        List<Pizza> filteredList = new ArrayList<>();
        for (Pizza pizza : originalPizzaList) {
            boolean matches = true;
            if (!selectedPrice.equals("Any")) {
                double price = Double.parseDouble(selectedPrice.replace("$", ""));
                matches = matches && (pizza.getPrice() <= price);
            }
            if (!selectedSize.equals("Any")) {
                matches = matches && pizza.getSize().equalsIgnoreCase(selectedSize);
            }
            if (!selectedCategory.equals("All")) {
                matches = matches && pizza.getCategory().equalsIgnoreCase(selectedCategory);
            }
            if (matches) {
                filteredList.add(pizza);
            }
        }
        pizzaAdapter.setPizzas(filteredList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterPizzasByCategory(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        pizzaAdapter.setPizzas(originalPizzaList);  // Reset to full list when no category is selected
    }

    private void filterPizzasByCategory(String category) {
        Log.d(TAG, "Filtering by category: " + category);
        List<Pizza> filteredList = new ArrayList<>();
        for (Pizza pizza : originalPizzaList) {
            Log.d(TAG, "Pizza Category: " + pizza.getCategory()); // Debug category
            if (pizza.getCategory().equalsIgnoreCase(category) || category.equals("All")) {
                filteredList.add(pizza);
            }
        }
        Log.d(TAG, "Filtered Count: " + filteredList.size()); // Debug count
        pizzaAdapter.setPizzas(filteredList);
    }

    // TextWatcher implementation to avoid creating anonymous classes
    private abstract static class SimpleTextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(android.text.Editable s) {}
    }
}

package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OrderDialogFragment extends DialogFragment {

    private Spinner sizeSpinner;
    private TextView priceTextView;
    private TextView quantityTextView;
    private Button decrementButton;
    private Button incrementButton;
    private Button submitButton;
    private ImageView pizzaImageView;

    private OnOrderSubmitListener listener;

    public interface OnOrderSubmitListener {
        void onOrderSubmit(String size, double price, int quantity);
    }

    public void setOnOrderSubmitListener(OnOrderSubmitListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_dialog, container, false);

        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        priceTextView = view.findViewById(R.id.priceTextView);
        quantityTextView = view.findViewById(R.id.quantityTextView);
        decrementButton = view.findViewById(R.id.decrementButton);
        incrementButton = view.findViewById(R.id.incrementButton);
        submitButton = view.findViewById(R.id.submitButton);
        pizzaImageView = view.findViewById(R.id.pizzaImage);

        // Set up the size spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);

        // Retrieve and display the base price and image
        double basePrice = getArguments().getDouble("base_price");
        String baseSize = getArguments().getString("base_size");
        int imageId = getArguments().getInt("image_id");
        pizzaImageView.setImageResource(imageId);

        // Set the default price and size
        sizeSpinner.setSelection(adapter.getPosition(baseSize));
        priceTextView.setText(String.format("$%.2f", basePrice));

        // Set up quantity buttons
        decrementButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTextView.getText().toString());
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
                updatePrice(basePrice, baseSize, sizeSpinner.getSelectedItem().toString(), quantity);
            }
        });

        incrementButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTextView.getText().toString());
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
            updatePrice(basePrice, baseSize, sizeSpinner.getSelectedItem().toString(), quantity);
        });

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePrice(basePrice, baseSize, sizeSpinner.getSelectedItem().toString(), Integer.parseInt(quantityTextView.getText().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up submit button
        submitButton.setOnClickListener(v -> {
            String size = sizeSpinner.getSelectedItem().toString();
            int quantity = Integer.parseInt(quantityTextView.getText().toString());
            double totalPrice = calculatePrice(basePrice, baseSize, size) * quantity;
            if (listener != null) {
                listener.onOrderSubmit(size, totalPrice, quantity);
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void updatePrice(double basePrice, String baseSize, String size, int quantity) {
        double price = calculatePrice(basePrice, baseSize, size) * quantity;
        priceTextView.setText(String.format("$%.2f", price));
    }

    private double calculatePrice(double basePrice, String baseSize, String size) {
        double price;
        switch (size) {
            case "Small":
                if (baseSize.equals("Medium")) {
                    price = basePrice / 1.5;
                } else if (baseSize.equals("Large")) {
                    price = basePrice / 2;
                } else {
                    price = basePrice;
                }
                break;
            case "Medium":
                if (baseSize.equals("Small")) {
                    price = basePrice * 1.5;
                } else if (baseSize.equals("Large")) {
                    price = basePrice / 2 * 1.5;
                } else {
                    price = basePrice;
                }
                break;
            case "Large":
                if (baseSize.equals("Small")) {
                    price = basePrice * 2;
                } else if (baseSize.equals("Medium")) {
                    price = basePrice / 1.5 * 2;
                } else {
                    price = basePrice;
                }
                break;
            default:
                price = basePrice;
                break;
        }
        return price;
    }
}

package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class AddSpecialOfferFragment extends Fragment {

    private Spinner spinnerOfferName;
    private Spinner spinnerPizzaType;
    private Spinner spinnerPizzaSize;
    private Spinner spinnerOfferPeriod;
    private EditText editTextTotalPrice;
    private Button buttonSaveOffer;
    private DataBaseHelper dataBaseHelper;

    public AddSpecialOfferFragment() {
        // Required empty public constructor
    }

    public static AddSpecialOfferFragment newInstance() {
        return new AddSpecialOfferFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_special_offer, container, false);

        spinnerOfferName = view.findViewById(R.id.spinner_offer_name);
        spinnerPizzaType = view.findViewById(R.id.spinner_pizza_type);
        spinnerPizzaSize = view.findViewById(R.id.spinner_pizza_size);
        spinnerOfferPeriod = view.findViewById(R.id.spinner_offer_period);
        editTextTotalPrice = view.findViewById(R.id.editText_total_price);
        buttonSaveOffer = view.findViewById(R.id.button_save_offer);

        dataBaseHelper = new DataBaseHelper(getContext());

        ArrayAdapter<CharSequence> offerNameAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.offer_names, android.R.layout.simple_spinner_item);
        offerNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfferName.setAdapter(offerNameAdapter);

        ArrayAdapter<CharSequence> pizzaTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_categories, android.R.layout.simple_spinner_item);
        pizzaTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaType.setAdapter(pizzaTypeAdapter);

        ArrayAdapter<CharSequence> pizzaSizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pizza_sizes, android.R.layout.simple_spinner_item);
        pizzaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaSize.setAdapter(pizzaSizeAdapter);

        ArrayAdapter<CharSequence> offerPeriodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.offer_period, android.R.layout.simple_spinner_item);
        offerPeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfferPeriod.setAdapter(offerPeriodAdapter);

        buttonSaveOffer.setOnClickListener(v -> saveSpecialOffer());

        return view;
    }

    public void saveSpecialOffer() {
        String offerName = spinnerOfferName.getSelectedItem().toString();
        String pizzaType = spinnerPizzaType.getSelectedItem().toString();
        String pizzaSize = spinnerPizzaSize.getSelectedItem().toString();
        String offerPeriod = spinnerOfferPeriod.getSelectedItem().toString();
        String totalPriceStr = editTextTotalPrice.getText().toString();

        if (totalPriceStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter the total price", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(totalPriceStr);

        if (totalPrice == 0) {
            Toast.makeText(getContext(), "Total price cannot be zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an offer with the same attributes already exists
        SpecialOffer existingOffer = dataBaseHelper.getSpecialOfferByAttributes(offerName, pizzaType, pizzaSize, offerPeriod);

        if (existingOffer != null) {
            // Update the total price of the existing offer
            existingOffer.setTotalPrice(totalPrice);
            dataBaseHelper.updateSpecialOffer(existingOffer);
            dataBaseHelper.applyOffer(existingOffer);
            Toast.makeText(getContext(), "Special offer updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Add a new offer
            SpecialOffer specialOffer = new SpecialOffer(0, offerName, pizzaType, pizzaSize, offerPeriod, totalPrice);
            long offerId = dataBaseHelper.addSpecialOffer(specialOffer);

            // Apply the new offer
            dataBaseHelper.applyOffer(specialOffer);

            List<Pizza> pizzas = dataBaseHelper.getPizzasByTypeAndSize(pizzaType, pizzaSize);
            for (Pizza pizza : pizzas) {
                dataBaseHelper.addPizzaToOffer((int) offerId, pizza.getID());
            }

            Toast.makeText(getContext(), "Special offer added successfully", Toast.LENGTH_SHORT).show();
        }
    }

}

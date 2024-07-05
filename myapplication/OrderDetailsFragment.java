package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class OrderDetailsFragment extends Fragment {
    private static final String ARG_ORDER = "order";

    private Order order;
    private DataBaseHelper db;

    public static OrderDetailsFragment newInstance(Order order) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order); // Corrected this line
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }

        db = new DataBaseHelper(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order Details");
        }
        setHasOptionsMenu(true);

        ImageView orderImage = view.findViewById(R.id.orderImage);
        TextView orderName = view.findViewById(R.id.orderName);
        TextView orderDetails = view.findViewById(R.id.orderDetails);
        TextView orderDate = view.findViewById(R.id.orderDate);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        if (order != null) {
            orderImage.setImageResource(order.getImageId());

            // Extracting pizza name and other details
            String[] details = order.getOrderDetails().split(", ");
            String pizzaName = details[0].split(": ")[1];
            String size = details[1].split(": ")[1];
            String quantity = details[2].split(": ")[1];
            String price = String.format("%.2f", Double.parseDouble(details[3].split(": ")[1]));

            orderName.setText(pizzaName);
            orderDetails.setText("Size: " + size + "\nQuantity: " + quantity + "\nTotal Price: $" + price);
            orderDate.setText(order.getOrderDate());

            cancelButton.setOnClickListener(v -> {
                db.deleteOrder(order.getOrderId());
                getActivity().getSupportFragmentManager().popBackStack();
            });
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
